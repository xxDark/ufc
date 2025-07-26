package dev.xdark.ufc;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

final class CompactUninterruptiblePath implements Path {
	final Path delegate;
	private final CompactUninterruptibleFileSystem fileSystem;
	final OpenOption[] originalOptions;
	SeekableByteChannel channel;
	boolean restoreOriginalOptions;

	SeekableByteChannel asByteChannel() {
		return channel;
	}

	FileChannel asFileChannel() throws IOException {
		var channel = this.channel;
		if (!(channel instanceof FileChannel)) {
			channel.close();
			throw new IllegalStateException("Unable to retrieve FileChannel");
		}
		return (FileChannel) channel;
	}

	CompactUninterruptiblePath(Path delegate, OpenOption[] openOptions, CompactUninterruptibleFileSystem fileSystem) {
		this.delegate = delegate;
		originalOptions = openOptions.clone();
		this.fileSystem = fileSystem;
	}

	CompactUninterruptiblePath(Path delegate, OpenOption[] openOptions) {
		this(delegate, openOptions, new CompactUninterruptibleFileSystem(delegate.getFileSystem()));
	}

	@Override
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public boolean isAbsolute() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getRoot() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getFileName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getParent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNameCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getName(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean startsWith(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean endsWith(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path normalize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path resolve(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path relativize(Path other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URI toUri() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path toAbsolutePath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events,
			WatchEvent.Modifier... modifiers) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Path other) {
		throw new UnsupportedOperationException();
	}
}
