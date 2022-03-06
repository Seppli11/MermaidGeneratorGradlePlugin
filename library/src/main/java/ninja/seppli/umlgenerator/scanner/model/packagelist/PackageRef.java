package ninja.seppli.umlgenerator.scanner.model.packagelist;

public record PackageRef(String packagePath) {
    public boolean containsPackage(String otherPackagePath) {
        return otherPackagePath.startsWith(packagePath);
    }

    public boolean containsPackage(PackageRef otherPackagePath) {
        return containsPackage(otherPackagePath.packagePath());
    }
}
