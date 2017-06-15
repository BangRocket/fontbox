package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.layout.components.Line;

import java.io.IOException;

public interface ILineWriter {

    /**
     * Called to emit the stack's contents to a Line element. The contents of
     * the stack are automatically cleared and zerored on invocation.
     *
     * @return The formatted line. The stack, properties and other values
     * associated with generating the line are reset on the self object.
     */
    IElement emit();

    /**
     * Get the current pending bounding box of the words on the writer.
     *
     * @return The pending bounding box of the words on the writer.
     */
    ObjectBounds pendingBounds();

    /**
     * Pushes the word onto the writer stack. The word is placed on the end of
     * the stack and the dimensions of the stack are recomputed automatically.
     *
     * @param word The word to place on the end of the stack
     * @param ignoreInvalidSymbols don't throw exceptions for unsupported symbols
     * @throws IOException     Any exception which occurs when reading from the page writing
     *                         stream underlying this writer
     * @throws LayoutException Any exception which occurs when updating the potentially
     *                         paginated text
     */
    void push(String word, boolean ignoreInvalidSymbols) throws LayoutException, IOException;

    /**
     * Removes the word from the end of the writer stack. The word removed is
     * returned and then dimensions of the stack are recomputed automatically.
     *
     * @param ignoreInvalidSymbols don't throw exceptions for unsupported symbols
     * @return The word which was removed from the end of the stack
     * @throws IOException     Any exception which occurs when reading from the page writing
     *                         stream underlying this writer
     * @throws LayoutException Any exception which occurs when updating the potentially
     *                         paginated text
     */
    String pop(boolean ignoreInvalidSymbols) throws LayoutException, IOException;

    /**
     * Get the size (number of elements) on the stack at the current time.
     *
     * @return The number of elements currently on the writer stack at the time
     * of invocation.
     */
    int size();
}
