package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.IDocumentBuilder;
import net.afterlifelochie.fontbox.api.data.IDocument;

public class DocumentBuilder implements IDocumentBuilder {
    @Override
    public IDocument createDocument() {
        return new DocumentWrapper();
    }
}
