import subprocess
import sys

IMPL_TYPE = sys.argv[1]
ACTION = sys.argv[2]

OUT_FILE = open('test_results_' + IMPL_TYPE + '_' + ACTION + '.out','w')
# ACTIONS = ["KH", "AE", "DE", "AV", "DV"]

K_VALS = [3,6,9]

graph_files = subprocess.run(["ls","graphs"],capture_output = True)
graph_files = graph_files.stdout.decode('utf-8').split()

if (ACTION != "KH"):
        for graph_file in graph_files:
                # for action in ACTIONS:
                input_file_name = graph_file[0:11] + '_' + ACTION + '_test.in'
                input_file_path = 'inTestFiles/' + input_file_name
                input_file = open(input_file_path, "rb")
                input_data = input_file.read()

              
                model = subprocess.run(['java', '-cp', '.:jopt-simple-5.0.2.jar', 'RmitCovidModelling',\
                        '-f', 'graphs/' + graph_file, IMPL_TYPE], input = input_data, capture_output=True)
                outstr = input_file_name + "\t" + model.stdout.decode('utf-8').split()[-2] + ' ' + model.stdout.decode('utf-8').split()[-1] + '\n'
                # print(model)
                # outstr = input_file_name + "\t" + model.stdout.decode('utf-8') + '\n'

                print(outstr)
                OUT_FILE.write(outstr)

elif (ACTION == "KH"):
        small_graph_files = []
        for graph_file in graph_files:
                if(graph_file[0] == "S"):
                        small_graph_files.append(graph_file)

        for small_graph_file in small_graph_files:
                for k in K_VALS:
                        input_file_name = small_graph_file[0:11] + '_' + ACTION + str(k) + '_test.in'
                        input_file_path = 'inTestFiles/' + input_file_name
                        input_file = open(input_file_path, "rb")
                        input_data = input_file.read()

                        model = subprocess.run(['java', '-cp', '.:jopt-simple-5.0.2.jar', 'RmitCovidModelling',\
                                '-f', 'graphs/' + small_graph_file, IMPL_TYPE], input = input_data, capture_output=True)

                        # time = model.stdout.decode('utf-8').split()[-2] + ' ' + model.stdout.decode('utf-8').split()[-1]


                        outstr = input_file_name + "\t" + model.stdout.decode('utf-8') + "\n"
                        print(outstr)
                        OUT_FILE.write(outstr)

else:
        print("Error. No such test")

OUT_FILE.close()
# input_file.close()


