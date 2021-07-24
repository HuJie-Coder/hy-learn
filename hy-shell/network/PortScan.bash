#!/usr/bin/env bash

host="103.60.149.52"
start_port="1040"
end_port="65535"
export_dir="/Users/hujie/IdeaProjects/hy-learn/hy-shell/network/out"

nc -z -vv "${host}" "${start_port}-${end_port}" > port.txt 2>&1





