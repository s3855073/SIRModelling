import subprocess

graph_files = subprocess.run(["ls","graphs"],capture_output = True)
graph_files = graph_files.stdout.decode('utf-8').split()
small_graph_files = []

ACTIONS = ["AE", "DE", "AV", "DV"]

for graph_file in graph_files:
    for action in ACTIONS:
        # subprocess.run(["python3.8","./test_gen.py", graph_file, action])
        subprocess.run(["python","./test_gen.py", graph_file, action])
    if(graph_file[0] == "S"):
        small_graph_files.append(graph_file)

for small_graph_file in small_graph_files:
    subprocess.run(["python","./test_gen.py", small_graph_file, "KH"])
    



