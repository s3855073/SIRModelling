import itertools
import random
import sys

import os
CURR_DIR = os.getcwd()

# Opens graph file
GRAPH_FILE_NAME = str(sys.argv[1])
GRAPH_FILE = open(CURR_DIR + "/graphs/" + GRAPH_FILE_NAME, "r")

# Specifies which test to run
TEST_TYPE = str(sys.argv[2])

OUT_FILE_NAME = GRAPH_FILE_NAME[0:11] + "_" + TEST_TYPE + "_test.in"
OUT_FILE = open(CURR_DIR + "/inTestFiles/" + OUT_FILE_NAME, "w")

# How many tests iterations?
NUM_TESTS = 50

verts = []
edges = []
max_vert = 0

readingEdges = False
for line in GRAPH_FILE:
    tokens = line.split()
    if(tokens[0] == "*Edges"):
        readingEdges = True
        continue
    
    if(tokens[0] == "*Vertices"):
        max_vert = int(tokens[1])
        verts = []
        verts.extend(range(1,max_vert+1,1))
        # For test variability
        # random.shuffle(verts)
        
    elif(readingEdges):
        vert1 = int(tokens[0])
        vert2 = int(tokens[1])
        if(vert1<vert2):
            edges.append((vert1,vert2))
        else:
            edges.append((vert2,vert1))

def gen_av_cmds():
    test_count = 0
    for i in range(max_vert + 1, max_vert + 1 + NUM_TESTS):
        test_count += 1
        OUT_FILE.write("AV " + str(i) + "\n")
        if(test_count>NUM_TESTS): 
            break

def gen_dv_cmds():
    for v in random.sample(verts, k=NUM_TESTS):
        OUT_FILE.write("DV " + str(v) + "\n")

def gen_ae_cmds():
    combs = list(itertools.combinations(verts, 2))
    
    valid_edges = []
    SAMPLE_SIZE = 1000

    sampled_edges = random.sample(combs, k=SAMPLE_SIZE)
    for edge in sampled_edges:
        # sort tuple before comparison
        if(edge[0] > edge[1]):
            edge = (edge[1],edge[0])
        if(edge not in edges):
            valid_edges.append(edge)

    test_count = 0
    # random.sample to add some edge adding variability
    for edge in valid_edges:
        test_count += 1
        OUT_FILE.write("AE " + str(edge[0]) + " " + str(edge[1]) + "\n")
        if(test_count >= NUM_TESTS):
            break
            

def gen_de_cmds():
    for edge in random.sample(edges,k=30):
        OUT_FILE.write("DE " + str(edge[0]) + " " + str(edge[1]) + "\n")

K_VALS = [3, 6, 9]
def gen_khop_cmds():
    for k in K_VALS:
        OUT_FILE_NAME = GRAPH_FILE_NAME[0:11] + "_KH" + str(k) + "_test.in"
        OUT_FILE = open(CURR_DIR + "/inTestFiles/" + OUT_FILE_NAME, "w")
        
        for vert in random.sample(verts, k=NUM_TESTS):    
                OUT_FILE.write("KN " + str(k) + " " + str(vert) + "\n")

# Choose what to generate
if(TEST_TYPE == "KH"):
    gen_khop_cmds()
elif(TEST_TYPE == "AE"):
    gen_ae_cmds()
elif(TEST_TYPE == "DE"):
    gen_de_cmds()
elif(TEST_TYPE == "AV"):
    gen_av_cmds()
elif(TEST_TYPE == "DV"):
    gen_dv_cmds()
else:
    print("Error: no such test")

GRAPH_FILE.close()
OUT_FILE.close()




