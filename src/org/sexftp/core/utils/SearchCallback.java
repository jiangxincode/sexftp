package org.sexftp.core.utils;

import sexftp.views.AbstractSexftpView;

public abstract interface SearchCallback {
	public abstract TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject paramTreeObject);
}
