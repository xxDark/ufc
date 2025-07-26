package dev.xdark.ufc;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;

/// @see UninterruptibleChannels
public final class UninterruptibleFiles {
	private UninterruptibleFiles() {
	}

	/// @param path Path.
	/// @return Uninterruptible path.
	public static Path newPath(Path path) {
		return new UninterruptiblePath(path);
	}

	/// @param fileSystem File system.
	/// @return Uninterruptible file system.
	/// @implNote Currently it is impossible to open channels with present {@link FileAttribute}s.
	public static FileSystem newFileSystem(FileSystem fileSystem) {
		return new UninterruptibleFileSystem(fileSystem);
	}

	/// @param fileSystemProvider File system provider.
	/// @return Uninterruptible file system provider.
	/// @implNote Currently it is impossible to open channels with present {@link FileAttribute}s.
	public static FileSystemProvider newFileSystemProvider(FileSystemProvider fileSystemProvider) {
		return new UninterruptibleFileSystemProvider(fileSystemProvider);
	}
}
