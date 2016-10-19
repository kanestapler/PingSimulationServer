import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class RespondThread extends Thread {
	
	private DatagramPacket packet;
	
	public RespondThread(DatagramPacket packet) {
		this.packet = packet;
	}
	
	public void run() {

		System.out.println ("Packet received at " + new Date( ));
		// Display packet information
		InetAddress remote_addr = packet.getAddress();
		System.out.println ("Sender: " + remote_addr.getHostAddress( ) );
		System.out.println ("from Port: " + packet.getPort());

		// Display packet contents, by reading from byte array
		ByteArrayInputStream bin = new ByteArrayInputStream (packet.getData());

		// Display only up to the length of the original UDP packet
		String endResult = "";
		for (int i=0; i < packet.getLength(); i++)  {
			int data = bin.read();
			if (data == -1) {
				break;
			}
			else {
				//Recieved 1 char correctly
				endResult = endResult + Character.toString((char) data);
				sendResponse((char) data, remote_addr.getHostAddress());
			}
		}
	}
	
	private void sendResponse(char x, String returnAddress) {
		System.out.println(x);
		String hostname=returnAddress;
		String message = Character.toString(x);
		try {
			// Create a datagram socket, look for the first available port
			@SuppressWarnings("resource")
			DatagramSocket socket = new DatagramSocket();

			System.out.println ("Using local port: " + socket.getLocalPort());
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			PrintStream pOut = new PrintStream(bOut);
			pOut.print(message);
			//convert printstream to byte array
			byte [ ] bArray = bOut.toByteArray();
			// Create a datagram packet, containing a maximum buffer of 256 bytes
			DatagramPacket packet=new DatagramPacket( bArray, bArray.length );

			System.out.println("Looking for hostname " + hostname);
			//get the InetAddress object
			InetAddress remote_addr = InetAddress.getByName(hostname);
			//check its IP number
			System.out.println("Hostname has IP address = " + 
					remote_addr.getHostAddress());
			//configure the DataGramPacket
			packet.setAddress(remote_addr);
			packet.setPort(2001);
			//send the packet
			socket.send(packet);

		}
		catch (UnknownHostException ue){
			System.out.println("Unknown host "+hostname);
		}
		catch (IOException e)	{
			System.out.println ("Error - " + e);
		}
	}
}
