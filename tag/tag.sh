#!/bin/bash

version=$(grep '^app.version=' application.properties | cut -d'=' -f2)
echo $version