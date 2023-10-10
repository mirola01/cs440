package luther;

public class Programs {
    private final String program;
    public Programs(String program) {
        this.program = program;
    }

    /**
     * @return Team name
     */
    public String getProgram() {
        return this.program;
    }

    @Override
    public String toString() {
        return String.format("%s", program);
    }
}
