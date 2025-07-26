package dev.xdark.ufc;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

final class UninterruptiblePath implements Path {
	private final Path delegate;
	private final UninterruptibleFileSystem fileSystem;

	UninterruptiblePath(Path delegate, UninterruptibleFileSystem fileSystem) {
		this.delegate = delegate;
		this.fileSystem = fileSystem;
	}

	UninterruptiblePath(Path delegate) {
		this(delegate, new UninterruptibleFileSystem(delegate.getFileSystem()));
	}

	private Path wrap(Path path) {
		if (path != null)
			path = new UninterruptiblePath(path, fileSystem);
		return path;
	}

	@Override
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public boolean isAbsolute() {
		return delegate.isAbsolute();
	}

	@Override
	public Path getRoot() {
		return wrap(delegate.getRoot());
	}

	@Override
	public Path getFileName() {
		return wrap(delegate.getFileName());
	}

	@Override
	public Path getParent() {
		return wrap(delegate.getParent());
	}

	@Override
	public int getNameCount() {
		return delegate.getNameCount();
	}

	@Override
	public Path getName(int index) {
		return wrap(delegate.getName(index));
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return wrap(delegate.subpath(beginIndex, endIndex));
	}

	@Override
	public boolean startsWith(Path other) {
		return delegate.startsWith(other);
	}

	@Override
	public boolean endsWith(Path other) {
		return delegate.endsWith(other);
	}

	@Override
	public Path normalize() {
		return wrap(delegate.normalize());
	}

	@Override
	public Path resolve(Path other) {
		return wrap(delegate.resolve(other));
	}

	@Override
	public Path relativize(Path other) {
		return wrap(delegate.relativize(other));
	}

	@Override
	public URI toUri() {
		return delegate.toUri();
	}

	@Override
	public Path toAbsolutePath() {
		return wrap(delegate.toAbsolutePath());
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		return wrap(delegate.toRealPath(options));
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events,
			WatchEvent.Modifier... modifiers) throws IOException {
		return delegate.register(watcher, events, modifiers);
	}

	@Override
	public int compareTo(Path other) {
		return delegate.compareTo(other);
	}

	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	static Path unwrap(Path path) {
		return ((UninterruptiblePath) path).delegate;
	}
}
