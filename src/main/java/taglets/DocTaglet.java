package taglets;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import jdk.javadoc.doclet.Taglet;

public abstract class DocTaglet implements Taglet {

    public Set<Location> getAllowedLocations() {
        return Set.of(Location.TYPE, Location.METHOD, Location.MODULE, Location.CONSTRUCTOR, Location.FIELD);
    }

    public boolean isInlineTag() {
        return false;
    }

    @Override
    public abstract String getName();

    protected abstract String getHeader();

    protected abstract String getBaseDocURI();

    public String toString(List<? extends DocTree> tags, Element element) {
        if (tags.size() == 0) {
            return null;
        }

        StringBuilder buf = new StringBuilder(String.format("<dl><dt><span class=\"strong\">%s</span></dt>", getHeader()));
        for (DocTree tag : tags) {
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
