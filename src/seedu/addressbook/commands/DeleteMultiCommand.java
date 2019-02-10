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

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Persons:\n%1$s";

    public static final String MESSAGE_PERSON_NOT_IN_ADDRESSBOOK = "The person index %1$s provided is invalid";

    private final ArrayList<Integer> targetIndices;

    public DeleteMultiCommand(ArrayList<Integer> targetIndices) {
        super(targetIndices.get(0));
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute() {
        String outputTargets = "";
        try {
            for (Integer targetIndex: targetIndices) {
                setTargetIndex(targetIndex);
                ReadOnlyPerson target = getTargetPerson();
                addressBook.removePerson(target);
                outputTargets += " " + target + "\n";
            }
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, outputTargets));


        } catch (IndexOutOfBoundsException ie) {
            if (!outputTargets.isEmpty()) {
                return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, outputTargets) +
                    "\n" + String.format(MESSAGE_PERSON_NOT_IN_ADDRESSBOOK, getTargetIndex()));
            }
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (UniquePersonList.PersonNotFoundException pnfe) {
            if (!outputTargets.isEmpty()) {
                return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, outputTargets) +
                    "\n" + String.format(MESSAGE_PERSON_NOT_IN_ADDRESSBOOK, getTargetIndex()));
            }
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }
    }
}
