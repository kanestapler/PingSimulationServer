import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) {
		String message;
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(4400);
			while (true){
				Socket socket = server.accept();
				BufferedInputStream binst = new
						BufferedInputStream(socket.getInputStream(),
								socket.getReceiveBufferSize());
				System.out.print("\n RECEIVED MESSAGE: ");
				message = "";
				for (int i=0;i<socket.getReceiveBufferSize();i++) {
					int data = binst.read();
					if (data == -1) break;
					else{
						System.out.print ((char) data) ;
						message += (char) (data);
					}
				}
				message = message.trim();
				socket.close();
				if (message.equals("bye")){
					System.out.println("\n Closing ... goodbye!");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
	}

}