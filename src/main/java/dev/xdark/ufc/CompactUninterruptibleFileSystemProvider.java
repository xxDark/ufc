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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class CompactUninterruptibleFileSystemProvider extends FileSystemProvider {
	private final FileSystemProvider delegate;

	CompactUninterruptibleFileSystemProvider(FileSystemProvider delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getScheme() {
		return delegate.getScheme();
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Path getPath(URI uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options,
			FileAttribute<?>... attrs) throws IOException {
		var impl = (CompactUninterruptiblePath) path;
		if (impl.restoreOriginalOptions) {
			options = new HashSet<>(Arrays.asList(impl.originalOptions));
		}
		Path delegate = impl.delegate;
		return (FileChannel) (impl.channel = delegate.getFileSystem().provider().newFileChannel(delegate, options, attrs));
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
			FileAttribute<?>... attrs) throws IOException {
		var impl = (CompactUninterruptiblePath) path;
		if (impl.restoreOriginalOptions) {
			options = new HashSet<>(Arrays.asList(impl.originalOptions));
		}
		Path delegate = impl.delegate;
		return impl.channel = delegate.getFileSystem().provider().newByteChannel(delegate, options, attrs);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			DirectoryStream.Filter<? super Path> filter) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type,
			LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		throw new UnsupportedOperationException();
	}
}
