package seedu.addressbook.commands;

public class DeleteMultiCommand extends Command {

    public static final String COMMAND_WORD = "deletemulti";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes multiple persons identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Persons: %1$s";

    public DeleteMultiCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
    }
}
