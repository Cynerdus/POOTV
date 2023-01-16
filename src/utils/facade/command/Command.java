package utils.facade.command;

public interface Command {

    /**
     *      command processing
     */
    void process();

    /**
     *      command execution + edge cases treatment
     */
    void execute();
}
