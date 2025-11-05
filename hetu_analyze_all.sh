#!/bin/bash

# Script to analyze all .ht files in a project using hetu analyze command

# Check if hetu command is available
if ! command -v hetu &> /dev/null; then
    echo "Error: hetu command is not available in PATH"
    exit 1
fi

# Check if a directory is provided as argument, otherwise use current directory
if [ -z "$1" ]; then
    PROJECT_DIR="."
else
    PROJECT_DIR="$1"
fi

# Validate that the directory exists
if [ ! -d "$PROJECT_DIR" ]; then
    echo "Error: $PROJECT_DIR is not a directory"
    exit 1
fi

echo "Analyzing all .ht files in: $PROJECT_DIR"
echo "==============================================="

# Find all .ht files and analyze them
find "$PROJECT_DIR" -name "*.ht" -type f | while read -r ht_file; do
    echo "Analyzing: $ht_file"
    echo "-----------------------------------------------"
    hetu analyze "$ht_file"
    echo ""
    echo "==============================================="
done

echo "Analysis complete."