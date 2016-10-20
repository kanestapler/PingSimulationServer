import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RespondThread extends Thread {
	
	private DatagramPacket packet;
	private int threadNumber;
	private int secondPort = 2001;
	
	public RespondThread(DatagramPacket packet, int threadNumber) {
		this.packet = packet;
		this.threadNumber = threadNumber;
	}
	
	public void run() {
		// Display packet information
		InetAddress remote_addr = packet.getAddress();

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
				try {
					sendResponse((char) data, remote_addr.getHostAddress());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendResponse(char x, String returnAddress) throws InterruptedException {
		String hostname=returnAddress;
		String message = Character.toString(x);
		try {
			// Create a datagram socket, look for the first available port
			@SuppressWarnings("resource")
			DatagramSocket socket = new DatagramSocket();
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			PrintStream pOut = new PrintStream(bOut);
			pOut.print(message);
			//convert printstream to byte array
			byte [ ] bArray = bOut.toByteArray();
			// Create a datagram packet, containing a maximum buffer of 256 bytes
			DatagramPacket packet=new DatagramPacket( bArray, bArray.length );
			//get the InetAddress object
			InetAddress remote_addr = InetAddress.getByName(hostname);
			//check its IP number
			//configure the DataGramPacket
			packet.setAddress(remote_addr);
			packet.setPort(secondPort+threadNumber);
			//send the packet
			long millis = (long) (Math.random() * 20);
			Thread.sleep(millis);
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
