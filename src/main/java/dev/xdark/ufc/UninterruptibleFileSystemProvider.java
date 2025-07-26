package dev.xdark.ufc;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

final class UninterruptibleFileSystemProvider extends FileSystemProvider {
	private final FileSystemProvider delegate;

	UninterruptibleFileSystemProvider(FileSystemProvider delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getScheme() {
		return delegate.getScheme();
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		return new UninterruptibleFileSystem(this, delegate.newFileSystem(uri, env));
	}

	@Override
	public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
		return new UninterruptibleFileSystem(this, delegate.newFileSystem(path, env));
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		return new UninterruptibleFileSystem(this, delegate.getFileSystem(uri));
	}

	@Override
	public Path getPath(URI uri) {
		return new UninterruptiblePath(delegate.getPath(uri));
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
			FileAttribute<?>... attrs) throws IOException {
		if (attrs.length != 0) {
			throw new UnsupportedOperationException();
		}
		return UninterruptibleChannels.newByteChannel(unwrap(path), options);
	}

	@Override
	public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options,
			FileAttribute<?>... attrs) throws IOException {
		if (attrs.length != 0) {
			throw new UnsupportedOperationException();
		}
		return UninterruptibleChannels.newFileChannel(unwrap(path), options);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			DirectoryStream.Filter<? super Path> filter) throws IOException {
		return delegate.newDirectoryStream(unwrap(dir), filter);
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		delegate.createDirectory(unwrap(dir), attrs);
	}

	@Override
	public void delete(Path path) throws IOException {
		delegate.delete(unwrap(path));
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		delegate.copy(unwrap(source), unwrap(target), options);
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		delegate.move(unwrap(source), unwrap(target), options);
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		return delegate.isSameFile(unwrap(path), unwrap(path2));
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		return delegate.isHidden(unwrap(path));
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		return delegate.getFileStore(unwrap(path));
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		delegate.checkAccess(unwrap(path), modes);
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		return delegate.getFileAttributeView(unwrap(path), type, options);
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type,
			LinkOption... options) throws IOException {
		return delegate.readAttributes(unwrap(path), type, options);
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		return delegate.readAttributes(unwrap(path), attributes, options);
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		delegate.setAttribute(unwrap(path), attribute, value, options);
	}

	private static Path unwrap(Path path) {
		return ((UninterruptiblePath) path).delegate;
	}
}
