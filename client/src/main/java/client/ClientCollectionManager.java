package client;

import stored.LabWork;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClientCollectionManager {
    private static Vector<LabWork> clientCollection = new Vector<>();
    private static Comparator<LabWork> comparator = Comparator.comparing(LabWork::getName);
    private static Predicate<LabWork> filter = labWork -> true;

    public static Vector<LabWork> getClientCollection() {
        return clientCollection;
    }

    public static void setClientCollection(Vector<LabWork> collection) {
        clientCollection = collection.stream().sorted(comparator).filter(filter).collect(Collectors.toCollection(Vector::new));
    }

    public static LabWork getById(long id) {
        return clientCollection.stream().filter(lw -> lw.getId() == id).findFirst().orElse(null);
    }

    public static void setFilter(Predicate<LabWork> cond) {
        filter = cond;
    }

    public static void setComparator(Comparator c) {
        comparator = c;
    }
}
