package dev.xdark.ufc;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/// Dirty hack to create uninterruptible instances of {@link FileChannel}.
/// {@link FileChannel#open(Path, OpenOption...)} returns channels that could be interrupted.
/// This utility allows one to create a channel that will not do that.
/// The idea is to somehow expose the channel that gets open by either {@link Files#newInputStream(Path, OpenOption...)} or {@link Files#newOutputStream(Path, OpenOption...)}.
/// Standard implementation of {@link FileSystemProvider#newInputStream(Path, OpenOption...)} or {@link FileSystemProvider#newOutputStream(Path, OpenOption...)}
/// will call {@link FileChannelImpl#setUninterruptible()} for us.
///
/// Maybe it would be a great idea for JDK maintainers to give us access to uninterruptible channels for files? :)
public final class UninterruptibleChannels {
	private UninterruptibleChannels() {
	}

	/**
	 * @throws IllegalStateException If {@link SeekableByteChannel} could not be extracted.
	 * @see Files#newByteChannel(Path, OpenOption...)
	 */
	public static SeekableByteChannel newByteChannel(Path path, OpenOption... options) throws IOException {
		var pathWrapper = new CompactUninterruptiblePath(path, options, new CompactUninterruptibleFileSystem(path.getFileSystem()));
		pathWrapper.restoreOriginalOptions = true;
		Files.newInputStream(pathWrapper);
		return pathWrapper.asByteChannel();
	}

	/**
	 * @throws IllegalStateException If {@link SeekableByteChannel} could not be extracted.
	 * @see Files#newByteChannel(Path, OpenOption...)
	 */
	public static SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options) throws IOException {
		return newByteChannel(path, options.toArray(new OpenOption[0]));
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see FileChannel#open(Path, OpenOption...)
	 */
	public static FileChannel newFileChannel(Path path, OpenOption... options) throws IOException {
		var pathWrapper = new CompactUninterruptiblePath(path, options, new CompactUninterruptibleFileSystem(path.getFileSystem()));
		pathWrapper.restoreOriginalOptions = true;
		Files.newInputStream(pathWrapper);
		return pathWrapper.asFileChannel();
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see FileChannel#open(Path, OpenOption...)
	 */
	public static FileChannel newFileChannel(Path path, Set<? extends OpenOption> options) throws IOException {
		return newFileChannel(path, options.toArray(new OpenOption[0]));
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see Files#newInputStream(Path, OpenOption...)
	 */
	public static FileChannel newInputStream(Path path, OpenOption... options) throws IOException {
		var pathWrapper = new CompactUninterruptiblePath(path, options, new CompactUninterruptibleFileSystem(path.getFileSystem()));
		Files.newInputStream(pathWrapper, options);
		return pathWrapper.asFileChannel();
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see Files#newInputStream(Path, OpenOption...)
	 */
	public static FileChannel newInputStream(Path path, Set<? extends OpenOption> options) throws IOException {
		return newInputStream(path, options.toArray(new OpenOption[0]));
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see Files#newOutputStream(Path, OpenOption...)
	 */
	public static FileChannel newOutputStream(Path path, OpenOption... options) throws IOException {
		var pathWrapper = new CompactUninterruptiblePath(path, options, new CompactUninterruptibleFileSystem(path.getFileSystem()));
		Files.newOutputStream(pathWrapper, options);
		return pathWrapper.asFileChannel();
	}

	/**
	 * @throws IllegalStateException If {@link FileChannel} could not be extracted.
	 * @see Files#newOutputStream(Path, OpenOption...)
	 */
	public static FileChannel newOutputStream(Path path, Set<? extends OpenOption> options) throws IOException {
		return newOutputStream(path, options.toArray(new OpenOption[0]));
	}
}
