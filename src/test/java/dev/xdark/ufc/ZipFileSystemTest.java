package dev.xdark.ufc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.file.Files;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import static org.junit.jupiter.api.Assertions.*;

public class ZipFileSystemTest {

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void test(boolean uninterruptible) throws IOException {
		var path = Files.createTempFile("ufc", ".zip");
		try {
			final var entryName = "data";
			try (var zos = new ZipOutputStream(Files.newOutputStream(path))) {
				zos.putNextEntry(new ZipEntry(entryName));
				int length = 1024 * 1024;
				var data = new byte[length];
				ThreadLocalRandom.current().nextBytes(data);
				zos.write(data);
			}
			for (var provider : FileSystemProvider.installedProviders()) {
				if ("jar".equals(provider.getScheme())) {
					try (var fs = provider.newFileSystem(uninterruptible ? UninterruptibleFiles.newPath(path) : path, Map.of())) {
						var zipPath = fs.getPath(entryName);
						try (var in = Files.newInputStream(zipPath)) {
							Thread.currentThread().interrupt();
							try {
								in.readAllBytes();
								if (!uninterruptible) {
									fail("Expected ClosedByInterruptException");
								}
							} catch (ClosedByInterruptException ex) {
								if (uninterruptible) {
									fail("Did not expect ClosedByInterruptException");
								}
							}
						}
					}
					return;
				}
			}
			System.err.println("Couldn't find jar provider.");
		} finally {
			Files.deleteIfExists(path);
		}
	}
}
