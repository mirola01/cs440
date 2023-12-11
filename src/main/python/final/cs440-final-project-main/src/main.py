from typing import Dict, List, Tuple, Union
from collections import defaultdict

class Relation:
    def __init__(self, filename: str):
        self.data: Dict[str, List[Union[str, int, float]]] = self.load_data_from_csv(filename)

    def load_data_from_csv(self, filename: str) -> Dict[str, List[Union[str, int, float]]]:
        with open(filename, 'r') as file:
            lines = file.readlines()

        data = defaultdict(list)
        column_names = lines[0].strip().split(',')

        for column_name in column_names:
            data[column_name] = []

        for line in lines[1:]:
            row = line.strip().split(',')
            for i, column_name in enumerate(column_names):
                value = row[i]
                try:
                    float_value = float(value)
                    int_value = int(float_value)
                    if float_value == int_value:
                        data[column_name].append(int_value)
                    else:
                        data[column_name].append(float_value)
                except ValueError:
                    data[column_name].append(value)

        return data

    # Single-table operations:

    def project(self, cols: List[str]) -> Dict[str, List[Union[str, int, float]]]:
        project_data = {}
        if type(cols) != list:
            raise ValueError("projected fild names must be in list")
        for field_name in cols:
            if field_name in self.data.keys():
                project_data[field_name] = self.data[field_name]
            else:
                raise ValueError("invalid comumn name.")
        return project_data

    def rename(self, old: str, new: str) -> None:
        if old not in self.data:
            raise ValueError(f"Column '{old}' does not exist.")

        self.data[new] = self.data.pop(old)

    def extend(self, name: str, values: List[Union[str, int, float]] ) -> Dict[str, List[Union[str, int, float]]]:
        if len(values) == len(list(self.data.values())[1]):
            self.data[name] = values 
            return self.data
        else:
            raise IndexError('Invalid data for new field')

    def select(self, query: Dict[str, Union[str, int, float]]) -> Dict[str, List[Union[str, int, float]]]:
        result = {col: [] for col in self.data}  
        for i in range(len(next(iter(self.data.values())))): 
            row = {col: self.data[col][i] for col in self.data}
            if all(row[k] == v for k, v in query.items()):
                for col in self.data:
                    result[col].append(row[col])  
        return result

    def sort(self, cols: List[str], order: str = 'ascending') -> None:
        # Check if the order argument is valid
        if order not in ['ascending', 'descending']:
            raise ValueError(f"Invalid order: {order}")
        order_flag = order == 'descending'

        # Convert self.data.keys() to a list for indexing
        columns_list = list(self.data.keys())

        zipped_rows = list(zip(*[self.data[col] for col in columns_list]))
        sorted_rows = sorted(zipped_rows, key=lambda row: [row[columns_list.index(col)] for col in cols], reverse=order_flag)
        
        # Update self.data with sorted rows
        for i, col in enumerate(columns_list):
            self.data[col] = [row[i] for row in sorted_rows]

    def groupby(self, cols: Union[str, List[str]]) -> 'GroupBy':
        if isinstance(cols, str):
            cols = [cols]
        return GroupBy(self.data, cols)

    # Multi-table operations:

    def product(self, other: 'Relation') -> None:
        if not self.data or not other.data:
            # One of the relations is empty, resulting in an empty relation
            self.data = {}
            return

        new_data: Dict[str, List[Union[str, int, float]]] = defaultdict(list)

        for column_self, values_self in self.data.items():
            for column_other, values_other in other.data.items():
                new_column_name = column_self + '_' + column_other
                if new_column_name in new_data:
                    # Handle column name conflicts by adding a suffix to the column name
                    suffix = 1
                    while new_column_name in new_data:
                        new_column_name = column_self + '_' + column_other + '_' + str(suffix)
                        suffix += 1
                new_data[new_column_name] = values_self + values_other

        self.data = new_data

    def union(self, other: 'Relation') -> Dict[str, List[Union[str, int, float]]]: #me
        all_unioned_data = {}
        if len(self.data) == len(other.data):
            compatable_data = True
            for key in self.data.keys():
                if type(self.data[key][0]) != type(other.data[key][0]):
                    compatable_data = False
            if compatable_data:
                for key in self.data.keys():
                    all_unioned_data[key] = self.data[key] + other.data[key]
                all_rows = []
                for i in range(len(next(iter(all_unioned_data.values())))): 
                    row = {col: all_unioned_data[col][i] for col in self.data}
                    all_rows.append(row)
                rows = []
                rows.append(all_rows[0])
                for rw in all_rows:
                    newrow = True
                    for r in rows:
                        if rw == r:
                            newrow = False
                    if newrow:  
                        rows.append(rw)
                unioned_data = {}
                i = 0
                for key1 in rows[0]:
                    unioned_data[key1] = [rows[0][key1]]
                rows.pop(0)
                for d in rows:
                    for k in d:
                        unioned_data[k] = unioned_data[k] + [d[k]]
            
                return unioned_data
            else:
                raise ValueError("Invalid columns for Union.")                
        else:
            raise IndexError("Number of columns mus be the same.")

    def join(self, other: 'Relation', condition: Tuple[str, str]) -> None:
        if not self.data or not other.data:
            # One of the relations is empty, resulting in an empty relation
            self.data = {}
            return

        new_data: Dict[str, List[Union[str, int, float]]] = defaultdict(list)
        column_self, column_other = condition

        if column_self not in self.data:
            raise ValueError(f"Column '{column_self}' does not exist in the current relation.")

        if column_other not in other.data:
            raise ValueError(f"Column '{column_other}' does not exist in the other relation.")

        values_self = self.data[column_self]
        values_other = other.data[column_other]

        for i, value_self in enumerate(values_self):
            for j, value_other in enumerate(values_other):
                if value_self == value_other:
                    for column, values in self.data.items():
                        if column == column_self:
                            column = f"self.{column}"
                        new_data[column].append(values[i])
                    for column, values in other.data.items():
                        if column == column_other:
                            column = f"other.{column}"
                        new_data[column].append(values[j])

        self.data = new_data

    def semijoin(self, other: 'Relation', on: Tuple[str, str]) -> None:
        self_key, other_key = on
        if self_key not in self.data or other_key not in other.data:
            raise ValueError(f"One of the specified columns does not exist in the relations.")

        matching_rows = set(self.data[self_key]) & set(other.data[other_key])

        new_data = {key: [] for key in self.data.keys()}
        for i in range(len(self.data[self_key])):
            if self.data[self_key][i] in matching_rows:
                for key in self.data.keys():
                    new_data[key].append(self.data[key][i])

        self.data = new_data

    def antijoin(self, other: 'Relation', on: Tuple[str, str]) -> None:
        self_key, other_key = on
        if self_key not in self.data or other_key not in other.data:
            raise ValueError(f"One of the specified columns does not exist in the relations.")

        non_matching_rows = set(self.data[self_key]) - set(other.data[other_key])
        new_data = {}
        for key in self.data.keys():
            new_data[key] = [value for i, value in enumerate(self.data[key]) if self.data[self_key][i] in non_matching_rows]
        self.data = new_data

    def outerjoin(self, other: 'Relation') -> None:
        outerjoined_data = {}
        for skey in self.data:
            outerjoined_data[skey] = []
        for okey in other.data:
            outerjoined_data[okey] = []
        srows = []
        for i in range(len(next(iter(self.data.values())))): 
            row = {col: self.data[col][i] for col in self.data}
            srows.append(row)
      
        orows = []
        for i in range(len(next(iter(other.data.values())))): 
            row = {col: other.data[col][i] for col in other.data}
            orows.append(row)
        pk = ""
        for k in outerjoined_data:
            if (k in self.data) and (k in other.data):
                pk = k
        longer = srows
        if len(orows) > len(srows):
            longer = orows 
        i = 0     
        while i < len(longer):
            for key in outerjoined_data:
                if (len(srows) > i) and (key in srows[0]):
                    outerjoined_data[key] = outerjoined_data[key] + [srows[i][key]]
                elif (len(orows) > i) and (key in orows[0]):
                    outerjoined_data[key] = outerjoined_data[key] + [orows[i][key]]
                else:
                    outerjoined_data[key] = outerjoined_data[key] + [None]
            i += 1
        return(outerjoined_data)

class GroupBy:
    def __init__(self, data: Dict[str, List[Union[str, int, float]]], group_cols: List[str]):
        self.data = data
        self.group_cols = group_cols

    def count(self, col: str) -> Dict[Tuple[str, ...], int]:
        result = defaultdict(int)

        for i in range(len(self.data[col])):
            group_key = tuple(self.data[group_col][i] for group_col in self.group_cols)
            result[group_key] += 1

        return dict(result)

    def sum(self, col: str) -> Dict[Tuple[str, ...], Union[int, float]]:
        if isinstance(self.data[col][0], str):
            return {}

        result = defaultdict(int)

        for i in range(len(self.data[col])):
            group_key = tuple(self.data[group_col][i] for group_col in self.group_cols)
            result[group_key] += self.data[col][i]

        return dict(result)

    def min(self, col: str) -> Dict[Tuple[str, ...], Union[int, float]]:
        if isinstance(self.data[col][0], str):
            return {}

        result = defaultdict(lambda: float('inf'))

        for i in range(len(self.data[col])):
            group_key = tuple(self.data[group_col][i] for group_col in self.group_cols)
            result[group_key] = min(result[group_key], self.data[col][i])

        return dict(result)

    def max(self, col: str) -> Dict[Tuple[str, ...], Union[int, float]]:
        if isinstance(self.data[col][0], str):
            return {}

        result = defaultdict(lambda: float('-inf'))

        for i in range(len(self.data[col])):
            group_key = tuple(self.data[group_col][i] for group_col in self.group_cols)
            result[group_key] = max(result[group_key], self.data[col][i])

        return dict(result)


    def mean(self, col: str) -> Dict[Tuple[str, ...], float]:
        if isinstance(self.data[col][0], str):
            return {}

        sums = defaultdict(int)
        counts = defaultdict(int)

        for i in range(len(self.data[col])):
            group_key = tuple(self.data[group_col][i] for group_col in self.group_cols)
            sums[group_key] += self.data[col][i]
            counts[group_key] += 1

        result = {
            group_key: sums[group_key] / counts[group_key]
            for group_key in sums.keys()
        }
        return result

