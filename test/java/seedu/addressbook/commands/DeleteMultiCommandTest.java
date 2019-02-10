package seedu.addressbook.commands;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Address;
import seedu.addressbook.data.person.Email;
import seedu.addressbook.data.person.Name;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.Phone;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.ui.TextUi;
import seedu.addressbook.util.TestUtil;

public class DeleteMultiCommandTest {

    private AddressBook emptyAddressBook;
    private AddressBook addressBook;

    private List<ReadOnlyPerson> emptyDisplayList;
    private List<ReadOnlyPerson> listWithEveryone;
    private List<ReadOnlyPerson> listWithSurnameDoe;

    @Before
    public void setUp() throws Exception {
        Person johnDoe = new Person(new Name("John Doe"), new Phone("61234567", false),
            new Email("john@doe.com", false), new Address("395C Ben Road", false), Collections.emptySet());
        Person janeDoe = new Person(new Name("Jane Doe"), new Phone("91234567", false),
            new Email("jane@doe.com", false), new Address("33G Ohm Road", false), Collections.emptySet());
        Person samDoe = new Person(new Name("Sam Doe"), new Phone("63345566", false),
            new Email("sam@doe.com", false), new Address("55G Abc Road", false), Collections.emptySet());
        Person davidGrant = new Person(new Name("David Grant"), new Phone("61121122", false),
            new Email("david@grant.com", false), new Address("44H Define Road", false),
            Collections.emptySet());

        emptyAddressBook = TestUtil.createAddressBook();
        addressBook = TestUtil.createAddressBook(johnDoe, janeDoe, davidGrant, samDoe);

        emptyDisplayList = TestUtil.createList();

        listWithEveryone = TestUtil.createList(johnDoe, janeDoe, davidGrant, samDoe);
        listWithSurnameDoe = TestUtil.createList(johnDoe, janeDoe, samDoe);
    }

    @Test
    public void execute_emptyAddressBook_returnsPersonNotFoundMessage() {
        assertDeletionFailsDueToNoSuchPerson(new ArrayList<>(Arrays.asList(1)), emptyAddressBook, listWithEveryone);
    }

    @Test
    public void execute_noPersonDisplayed_returnsInvalidIndexMessage() {
        assertDeletionFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(1)), addressBook, emptyDisplayList);
    }

    @Test
    public void execute_targetPersonNotInAddressBook_returnsPersonNotFoundMessage()
        throws IllegalValueException {
        Person notInAddressBookPerson = new Person(new Name("Not In Book"), new Phone("63331444", false),
            new Email("notin@book.com", false), new Address("156D Grant Road", false), Collections.emptySet());
        List<ReadOnlyPerson> listWithPersonNotInAddressBook = TestUtil.createList(notInAddressBookPerson);

        assertDeletionFailsDueToNoSuchPerson(new ArrayList<>(Arrays.asList(1)), addressBook, listWithPersonNotInAddressBook);
    }

    @Test
    public void execute_invalidIndex_returnsInvalidIndexMessage() {
        assertDeletionFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(0)), addressBook, listWithEveryone);
        assertDeletionFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(-1)), addressBook, listWithEveryone);
        assertDeletionFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(listWithEveryone.size() + 1)), addressBook, listWithEveryone);
    }

    @Test
    public void execute_validInvalidIndex_personIsDeletedReturnsInvalidIndexMessage() throws PersonNotFoundException {
        assertDeletionSuccessFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(3, 0)),addressBook, listWithSurnameDoe);
        assertDeletionSuccessFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(3, -1)),addressBook, listWithSurnameDoe);
        assertDeletionSuccessFailsDueToInvalidIndex(new ArrayList<>(Arrays.asList(listWithSurnameDoe.size() + 1, 2)),addressBook, listWithSurnameDoe);
    }

    @Test
    public void execute_validIndex_personIsDeleted() throws PersonNotFoundException {
        assertDeletionSuccessful(new ArrayList<>(Arrays.asList(3, 1)), addressBook, listWithSurnameDoe);
        assertDeletionSuccessful(new ArrayList<>(Arrays.asList(listWithSurnameDoe.size())), addressBook, listWithSurnameDoe);

        int middleIndex = (listWithSurnameDoe.size() / 2) + 1;
        assertDeletionSuccessful(new ArrayList<>(Arrays.asList(middleIndex)), addressBook, listWithSurnameDoe);
    }

    /**
     * Creates a new delete command.
     *
     * @param targetIndices of the person that we want to delete
     */
    private DeleteMultiCommand createDeleteMultiCommand(ArrayList<Integer> targetIndices, AddressBook addressBook,
                                                        List<ReadOnlyPerson> displayList) {

        DeleteMultiCommand command = new DeleteMultiCommand(targetIndices);
        command.setData(addressBook, displayList);

        return command;
    }

    /**
     * Executes the command, and checks that the execution was what we had expected.
     */
    private void assertCommandBehaviour(DeleteMultiCommand deleteCommand, String expectedMessage,
                                        AddressBook expectedAddressBook, AddressBook actualAddressBook) {

        CommandResult result = deleteCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedAddressBook.getAllPersons(), actualAddressBook.getAllPersons());
    }

    /**
     * Asserts that the index is not valid for the given display list.
     */
    private void assertDeletionFailsDueToInvalidIndex(ArrayList<Integer> targetIndices, AddressBook addressBook,
                                                      List<ReadOnlyPerson> displayList) {

        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

        DeleteMultiCommand command = createDeleteMultiCommand(targetIndices, addressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, addressBook, addressBook);
    }

    /**
     * Asserts that the person at the specified index cannot be deleted, because that person
     * is not in the address book.
     */
    private void assertDeletionFailsDueToNoSuchPerson(ArrayList<Integer> targetIndices, AddressBook addressBook,
                                                      List<ReadOnlyPerson> displayList) {

        String expectedMessage = Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK;

        DeleteMultiCommand command = createDeleteMultiCommand(targetIndices, addressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, addressBook, addressBook);
    }

    /**
     * Asserts that a list of persons contained includes valid and invalid index,
     * valid index will be deleted successfully
     * invalid index for the given display list cannot be deleted.
     */
    private void assertDeletionSuccessFailsDueToInvalidIndex(ArrayList<Integer> targetIndices, AddressBook addressBook,
                                                             List<ReadOnlyPerson> displayList) throws PersonNotFoundException {
        String outputTargets = "";
        AddressBook expectedAddressBook = TestUtil.clone(addressBook);
        int lastIndex = 0;
        try {
            for (int i = 0; i < targetIndices.size(); i++) {
                lastIndex = targetIndices.get(i) - TextUi.DISPLAYED_INDEX_OFFSET;
                ReadOnlyPerson targetPerson = displayList.get(lastIndex);
                expectedAddressBook.removePerson(targetPerson);
                outputTargets += " " + targetPerson + "\n";
            }
        } catch (IndexOutOfBoundsException ie) {
            String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
            if (!outputTargets.isEmpty()) {
                expectedMessage = String.format(DeleteMultiCommand.MESSAGE_DELETE_PERSON_SUCCESS, outputTargets) + "\n"
                    + String.format(DeleteMultiCommand.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK, lastIndex + TextUi.DISPLAYED_INDEX_OFFSET);
            }

            AddressBook actualAddressBook = TestUtil.clone(addressBook);
            DeleteMultiCommand command = createDeleteMultiCommand(targetIndices, actualAddressBook, displayList);
            assertCommandBehaviour(command, expectedMessage, expectedAddressBook, actualAddressBook);
        }
    }

    /**
     * Asserts that the person at the specified index can be successfully deleted.
     *
     * The addressBook passed in will not be modified (no side effects).
     *
     * @throws PersonNotFoundException if the selected person is not in the address book
     */
    private void assertDeletionSuccessful(ArrayList<Integer> targetIndices, AddressBook addressBook,
                                          List<ReadOnlyPerson> displayList) throws PersonNotFoundException {

        String outputTargets = "";
        AddressBook expectedAddressBook = TestUtil.clone(addressBook);
        for (int i = 0; i < targetIndices.size(); i++) {
            ReadOnlyPerson targetPerson = displayList.get(targetIndices.get(i) - TextUi.DISPLAYED_INDEX_OFFSET);
            expectedAddressBook.removePerson(targetPerson);
            outputTargets += " " + targetPerson + "\n";
        }

        String expectedMessage = String.format(DeleteMultiCommand.MESSAGE_DELETE_PERSON_SUCCESS, outputTargets);

        AddressBook actualAddressBook = TestUtil.clone(addressBook);

        DeleteMultiCommand command = createDeleteMultiCommand(targetIndices, actualAddressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, expectedAddressBook, actualAddressBook);
    }
}
