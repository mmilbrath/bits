/*
 * BackupBits
 * @author Chuck Slate
 * @author Michael Milbrath
 */
package net.backupbits.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.backupbits.common.ChannelConnectException;
import net.backupbits.common.ChannelReadException;
import net.backupbits.common.ChannelWriteException;
import net.backupbits.common.FileHeader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * The Class CLI.
 */
public class CLI {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws ChannelConnectException
	 *             the channel connect exception
	 * @throws ChannelWriteException
	 *             the channel write exception
	 * @throws ChannelReadException
	 *             the channel read exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws ChannelConnectException,
			ChannelWriteException, ChannelReadException, IOException {

		Client client = new Client();

		final CommandLineParser parser = new PosixParser();
		// create the Options
		final Options options = new Options();
		options.addOption("q", "query", false, "query the backup server");
		options
				.addOption("r", "restore", true,
						"restore the specified file id");
		options.addOption("s", "start", false, "start the client monitor");

		try {
			final CommandLine line = parser.parse(options, args);

			// start the client
			if (line.hasOption("start")) {
				client.start();
			}

			// query the backup service
			if (line.hasOption("query")) {
				System.out.println(String.format("%10s%150s%10s%30s", "ID",
						"Name", "Size", "Upload Time"));

				for (final FileHeader h : client.getBackupService().query()) {
					System.out.println(String.format("%10s%150s%10s%30s", h
							.getFileID(), h.getFileName(), h.getFileSize(), h
							.getFileUploadTime()));
				}
			}

			// restore file id from backup service
			if (line.hasOption("restore")) {
				for (final FileHeader h : client.getBackupService().query()) {
					if (h.getFileID() == Integer.parseInt(line
							.getOptionValue("restore"))) {

						System.out.println(String.format("%10s%100s%10s%30s",
								"ID", "Name", "Size", "Upload Time"));
						System.out.println(String.format("%10s%100s%10s%30s", h
								.getFileID(), h.getFileName(), h.getFileSize(),
								h.getFileUploadTime()));

						final InputStream inputStream = client
								.getBackupService().restore(h.getFileID());

						final byte[] b = new byte[1024];
						int len = 0, current = 0;
						final FileOutputStream fos = new FileOutputStream(h
								.getFileName());
						while (((len = inputStream.read(b)) > 0)
								&& (current < h.getFileSize())) {
							current += len;
							fos.write(b, 0, len);
						}

						System.out.println(String.format(
								"%s of size %s restored", h.getFileName(),
								current));
					}
				}
			}
			// help message
			else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("cli", options);
			}
		} catch (final ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
		}
	}

}
