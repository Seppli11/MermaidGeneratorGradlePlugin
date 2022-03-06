package ninja.seppli.umlgenerator.scanner.model.packagelist;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PackageList extends AbstractList<PackageRef> {
    private List<PackageRef> internalList = new ArrayList<>();

    public PackageList() {
    }

    private PackageList(Collection<PackageRef> packageRefs) {
        internalList.addAll(packageRefs);
    }

    @Override
    public PackageRef get(int i) {
        return internalList.get(i);
    }

    public boolean containsPackageRef(String packagePath) {
        return stream().anyMatch(p -> p.containsPackage(packagePath));
    }

    public boolean containsPackageRef(PackageRef packageRef) {
        return containsPackageRef(packageRef.packagePath());
    }

    @Override
    public int size() {
        return internalList.size();
    }

    public static PackageList createFromPackagePaths(String... packagePaths) {
        return Arrays.stream(packagePaths).map(PackageRef::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), PackageList::new));
    }

    public static PackageList createFromPackageRefs(PackageRef... packageRefs) {
        return new PackageList(Arrays.asList(packageRefs));
    }

    public static PackageList createFromPackageRefs(Collection<PackageRef> packageRefs) {
        return new PackageList(packageRefs);
    }

}
