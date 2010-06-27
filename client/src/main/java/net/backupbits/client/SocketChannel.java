/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.backupbits.common.Channel;
import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.Instruction;

/**
 * The Class SocketChannel.
 */
public class SocketChannel implements Channel {

	/** The object input stream. */
	private ObjectInputStream objectInputStream;

	/** The object output stream. */
	private ObjectOutputStream objectOutputStream;

	/** The socket. */
	private Socket socket;

	/** The host. */
	private String host = "127.0.0.1";

	/** The port. */
	private int port = 4430;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.common.Channel#close()
	 */
	public void close() throws ChannelConnectException {
		if (socket != null) {
			try {
				socket.close();
			} catch (final Exception ex) {
				throw new ChannelConnectException();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.backupbits.common.Channel#getInputStream()
	 */
	public InputStream getInputStream() throws ChannelConnectException,
			ChannelReadException {
		try {
			openSocket();
			// return objectInputStream.read(buffer);
			// /socket.getOutputStream().write(data, 0, count);
			return socket.getInputStream();
			// System.out.println(socket.getInputStream().read(buffer));
			// return 0;
		} catch (final IOException ex) {
			close();
			throw new ChannelReadException();
		}
	}

	/**
	 * Open socket.
	 *
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 */
	private void openSocket() throws ChannelConnectException {
		if ((socket == null) || socket.isClosed()) {
			try {
				socket = new Socket(host, port);
				objectOutputStream = new ObjectOutputStream(socket
						.getOutputStream());
				objectInputStream = new ObjectInputStream(socket
						.getInputStream());
			} catch (final UnknownHostException ex) {
				close();
				throw new ChannelConnectException();
			} catch (final IOException ex) {
				close();

				throw new ChannelConnectException();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.common.Channel#read()
	 */
	public Instruction read() throws ChannelConnectException,
			ChannelReadException {
		try {
			openSocket();
			final Instruction i = (Instruction) objectInputStream.readObject();
			return i;
		} catch (final IOException ex) {
			close();
			throw new ChannelReadException();
		} catch (final ClassNotFoundException ex) {
			close();
			throw new ChannelReadException();
		}
	}

	/**
	 * Sets the host.
	 *
	 * @param host
	 *            the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Sets the port.
	 *
	 * @param port
	 *            the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer b = new StringBuffer();
		b.append(host + ":" + port);
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.backupbits.common.Channel#write(byte[], int)
	 */
	public void write(byte[] data, int count) throws ChannelConnectException,
			ChannelWriteException {
		try {
			openSocket();
			socket.getOutputStream().write(data, 0, count);
			objectOutputStream.flush();
		} catch (final IOException ex) {
			close();
			throw new ChannelWriteException();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.backupbits.common.Channel#write(com.backupbits.common.Instruction)
	 */
	public void write(Instruction i) throws ChannelConnectException,
			ChannelWriteException {
		try {
			openSocket();
			objectOutputStream.writeObject(i);
			objectOutputStream.flush();
		} catch (final IOException ex) {
			close();
			throw new ChannelWriteException();
		}
	}

}
