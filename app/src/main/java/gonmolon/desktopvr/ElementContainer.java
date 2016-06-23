package gonmolon.desktopvr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElementContainer {

    private HashMap<Integer, Element> elements;
    private int lastID;

    public ElementContainer() {
        elements = new HashMap<>();
        lastID = 0;
    }

    public Iterator getIterator() {
        return new ContainerIterator(this);
    }

    public void addElement(Element element) {
        elements.put(lastID++, element);
    }

    public void addElement(Element element, int ID) throws ContainerException {
        if(ID < 0) {
            throw new ContainerException(ContainerException.Error.ID_INVALID);
        } else if(elements.containsKey(ID)) {
            throw new ContainerException(ContainerException.Error.ID_USED);
        } else {
            if(ID >= lastID) {
                lastID = ID;
                addElement(element);
            } else {
                elements.put(ID, element);
            }
        }
    }

    public void removeElement(int ID) throws ContainerException {
        if(!checkID(ID)) {
            throw new ContainerException(ContainerException.Error.ID_INVALID);
        } else if (elements.containsKey(ID)) {
            elements.remove(ID);
        } else {
            throw new ContainerException(ContainerException.Error.ID_NONEXISTENT);
        }
    }

    public Element getElement(int ID) throws ContainerException {
        if (!checkID(ID)) {
            throw new ContainerException(ContainerException.Error.ID_INVALID);
        } else if (elements.containsKey(ID)) {
            return elements.get(ID);
        } else {
            throw new ContainerException(ContainerException.Error.ID_NONEXISTENT);
        }
    }

    private boolean checkID(int ID) {
        return ID >= 0 && ID < lastID;
    }

    public class ContainerIterator implements Iterator {

        private Iterator<Map.Entry<Integer, Element>> iterator;

        private ContainerIterator(ElementContainer container) {
            iterator = container.elements.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Element next() {
            return iterator.next().getValue();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
