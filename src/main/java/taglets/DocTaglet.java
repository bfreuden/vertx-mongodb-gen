package taglets;

import com.sun.javadoc.Tag;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.tools.doclets.Taglet;

public abstract class DocTaglet implements Taglet {
    @Override
    public boolean inField() {
        return true;
    }

    @Override
    public boolean inConstructor() {
        return true;
    }

    @Override
    public boolean inMethod() {
        return true;
    }

    @Override
    public boolean inOverview() {
        return true;
    }

    @Override
    public boolean inPackage() {
        return true;
    }

    @Override
    public boolean inType() {
        return true;
    }

    @Override
    public boolean isInlineTag() {
        return false;
    }

    @Override
    public abstract String getName();

    protected abstract String getHeader();

    protected abstract String getBaseDocURI();

    @Override
    public String toString(Tag tag) {
        return toString(new Tag[]{ tag} );
    }

    @Override
    public String toString(Tag[] tags) {
        if (tags.length == 0) {
            return null;
        }

        StringBuilder buf = new StringBuilder(String.format("<dl><dt><span class=\"strong\">%s</span></dt>", getHeader()));
        for (Tag tag : tags) {
            String text = ((UnknownBlockTagTree) tag).getContent().get(0).toString();
            buf.append("<dd>").append(genLink(text)).append("</dd>");
        }
        return buf.toString();
    }

    protected String genLink(final String text) {
        String relativePath = text;
        String display = text;

        int firstSpace = text.indexOf(' ');
        if (firstSpace != -1) {
            relativePath = text.substring(0, firstSpace);
            display = text.substring(firstSpace, text.length()).trim();
        }

        return String.format("<a href='%s%s'>%s</a>", getBaseDocURI(), relativePath, display);
    }
}
