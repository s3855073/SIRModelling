import sys
import os
CURR_DIR = os.getcwd()

# arg specifies which graph type to gen (ERDREN/ SCALEF)
graph_type = sys.argv[1]
OUT_PATH = 'pajek-cmds/' + graph_type + '_GEN.log'

OUT_FILE = open(OUT_PATH,'w')

# Change this to modify what we define as small medium or large
GRAPH_SIZE = {"S":"1000", "M":"3000", "L":"5000"}
DEGS = {"S":"1", "M":"3", "L":"5"}

CONFIG = """\
NETBEGIN 1
CLUBEGIN 1
PERBEGIN 1
CLSBEGIN 1
HIEBEGIN 1
VECBEGIN 1
"""
OUT_FILE.write(CONFIG)

gen_str = ""

label = 0
i = 0
for size, size_val in GRAPH_SIZE.items():
    for deg, deg_val in DEGS.items():   
        label += 1

        if(graph_type == "ERDREN"):
            for j in range(3):
                i+=1
                gen_str += \
                "N "+ str(i) +" " + graph_type + "  ["+ size_val +", "+ deg_val +", 1, 0] ("+ size_val +")\n"
                
                file_dir = CURR_DIR + "\graphs\\" + size + deg + "_" + graph_type + "_" + str(j) + ".net"
                gen_str += "N "+ str(i) +" WN \"" + file_dir + "\" 0 \n"

        elif(graph_type == "SCALEF"):
            for j in range(3):
                if(int(deg_val) >= 2):
                    i+=1
                    gen_str += \
                    "N "+ str(i) +" SCALEF  [2 1 "+ size_val +" 999999999 2 "+ deg_val +".00000 0.99000 0.49000 0.49000] ("+ size_val +")\n"
                    
                    file_dir = CURR_DIR + "\graphs\\" + size + deg + "_" + graph_type + "_" + str(j) + ".net"
                    gen_str += "N "+ str(i) +" WN \"" + file_dir + "\" 0 ("+ size_val +")\n"

OUT_FILE.write(gen_str)

OUT_FILE.close()
