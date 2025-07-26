package dev.xdark.ufc;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.Set;

final class UninterruptibleFileSystem extends FileSystem {
	private final UninterruptibleFileSystemProvider provider;
	private final FileSystem delegate;

	UninterruptibleFileSystem(UninterruptibleFileSystemProvider provider, FileSystem delegate) {
		this.provider = provider;
		this.delegate = delegate;
	}

	UninterruptibleFileSystem(FileSystem delegate) {
		this(new UninterruptibleFileSystemProvider(delegate.provider()), delegate);
	}

	@Override
	public FileSystemProvider provider() {
		return provider;
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}

	@Override
	public boolean isOpen() {
		return delegate.isOpen();
	}

	@Override
	public boolean isReadOnly() {
		return delegate.isReadOnly();
	}

	@Override
	public String getSeparator() {
		return delegate.getSeparator();
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		Iterable<Path> rootDirectories = delegate.getRootDirectories();
		return () -> {
			Iterator<Path> iterator = rootDirectories.iterator();
			return new Iterator<>() {
				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				@Override
				public Path next() {
					return new UninterruptiblePath(iterator.next(), UninterruptibleFileSystem.this);
				}
			};
		};
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return delegate.getFileStores();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return delegate.supportedFileAttributeViews();
	}

	@Override
	public Path getPath(String first, String... more) {
		return new UninterruptiblePath(delegate.getPath(first, more), this);
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		return delegate.getPathMatcher(syntaxAndPattern);
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		return delegate.getUserPrincipalLookupService();
	}

	@Override
	public WatchService newWatchService() throws IOException {
		return delegate.newWatchService();
	}
}
