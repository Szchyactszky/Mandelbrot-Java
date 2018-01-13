# Mandelbrot Java
Java Project to Calculate Mandelbrot image in Client-Server scenario

Calculates the image by given parameters, size of image and servers to distribute workload.

#How to use?
1. start the server(s) in commandline srv.class is found in bin folder. Remember the ports you specified.
2. start client in commandline Main.class is found in bin folder.
3. input the values for the programm in following format: min_c_re min_c_im max_c_re max_c_im iterations width_px height_px divisions server_1 server_2 server_N
example: -1 -1.5 2 1.5 1024 1000 1000 4 127.0.0.1:5556 127.0.0.1:5557



#TODO
multitreading for client and workload distribution across servers.
