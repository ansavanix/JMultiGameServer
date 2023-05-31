#!/bin/bash
set -e

cd "$(dirname "$0")"

javac multigame/server/Server.java
javac multigame/client/Client.java

jar -cfm Server.jar ServerManifest.txt multigame/server/*.class multigame/shared/*.class multigame/games/*.class
jar -cfm Client.jar ClientManifest.txt multigame/client/*.class multigame/shared/*.class
