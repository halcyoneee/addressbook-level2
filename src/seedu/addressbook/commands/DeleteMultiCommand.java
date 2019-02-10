package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;

import java.util.ArrayList;

public class DeleteMultiCommand extends Command {

    public static final String COMMAND_WORD = "deletemulti";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes multiple persons identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Persons: %1$s";

    private final ArrayList<Integer> targetIndices;

    public DeleteMultiCommand(ArrayList<Integer> targetIndices) {
        super(targetIndices.get(0));
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = getTargetPerson();
            addressBook.removePerson(target);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, target));

        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (UniquePersonList.PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }
    }
}
