#!/usr/bin/env bash
set -e
BASE=${1:-http://localhost:8080}

echo '--- home ---'
curl -s "$BASE/api/home" ; echo

echo '--- inventory ---'
curl -s "$BASE/api/inventory" ; echo

echo '--- set preference ---'
curl -s -X PUT "$BASE/api/preference" \
  -H 'Content-Type: application/json' \
  -d '{"healthGoal":"MUSCLE_BUILDING"}' ; echo

echo '--- recommendations ---'
curl -s "$BASE/api/recommendations" ; echo

echo 'Replace <recipe-id> below with a real id from recommendations output.'
