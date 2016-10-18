import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
		for (int i=0; i < packet.getLength(); i++)  {
			int data = bin.read();
			if (data == -1)
				break;
			else
				System.out.print ( (char) data) ;
		}
	}
}
