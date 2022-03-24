package ninja.seppli.umlgenerator.scanner.model.packagelist;
public class PackageList extends java.util.AbstractList<ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef> {
    private java.util.List<ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef> internalList = new java.util.ArrayList<>();

    public PackageList() {
    }

    private PackageList(java.util.Collection<ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef> packageRefs) {
        internalList.addAll(packageRefs);
    }

    @java.lang.Override
    public ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef get(int i) {
        return internalList.get(i);
    }

    public boolean containsPackageRef(java.lang.String packagePath) {
        return stream().anyMatch(( p) -> p.containsPackage(packagePath));
    }

    public boolean containsPackageRef(ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef packageRef) {
        return containsPackageRef(packageRef.packagePath());
    }

    @java.lang.Override
    public int size() {
        return internalList.size();
    }

    public static ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList createFromPackagePaths(java.lang.String... packagePaths) {
        return java.util.Arrays.stream(packagePaths).map(ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef::new).collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toList(), ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList::new));
    }

    public static ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList createFromPackageRefs(ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef... packageRefs) {
        return new ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList(java.util.Arrays.asList(packageRefs));
    }

    public static ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList createFromPackageRefs(java.util.Collection<ninja.seppli.umlgenerator.scanner.model.packagelist.PackageRef> packageRefs) {
        return new ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList(packageRefs);
    }
}