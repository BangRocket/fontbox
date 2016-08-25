package net.afterlifelochie.fontbox.api;

import net.afterlifelochie.fontbox.api.data.IDocument;

public interface IDocumentBuilder
{
    /**
     * @return A new {@link IDocument}
     */
    IDocument createDocument();
}
