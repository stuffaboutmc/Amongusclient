# Delete everything that's wrong
rm -rf src/main/java/myau/client/modules
rm -rf src/main/java/myau/client/module
rm -rf src/main/java/com

# Recreate correct structure
mkdir -p src/main/java/myau/client/module/impl
mkdir -p src/main/java/myau/client/settings
mkdir -p src/main/java/myau/client/core
mkdir -p src/main/java/myau/client/gui
mkdir -p src/main/java/myau/client/font
