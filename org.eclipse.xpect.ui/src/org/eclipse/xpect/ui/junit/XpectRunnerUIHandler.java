package org.eclipse.xpect.ui.junit;

import java.util.Collections;
import java.util.Map;

import org.eclipse.compare.CompareUI;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestSuiteElement;
import org.eclipse.jdt.junit.runners.IRunnerUIHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.xtext.util.Strings;

import com.google.common.collect.Maps;

@SuppressWarnings("restriction")
public class XpectRunnerUIHandler implements IRunnerUIHandler {

	private static class CompareAction extends Action {

		private ITestElement ctx;

		public CompareAction(ITestElement ctx) {
			super();
			this.ctx = ctx;
			setText("Compare with Expectation");
			setToolTipText("Compare test expecation with actual test result.");
		}

		@Override
		public boolean isEnabled() {
			return ctx instanceof TestElement && ((TestElement) ctx).isComparisonFailure();
		}

		@Override
		public void run() {
			FailureCompareEditorInput inp = new FailureCompareEditorInput(ctx);
			CompareUI.openCompareEditor(inp);
		}
	}

	@Override
	public void contextMenuAboutToShow(ITestElement ctx, IMenuManager menu) {
		menu.add(new CompareAction(ctx));
	}

	protected Map<String, String> parseName(String name) {
		if (Strings.isEmpty(name))
			return Collections.emptyMap();
		Map<String, String> result = Maps.newHashMap();
		for (String segment : name.split(";")) {
			String items[] = segment.split("=", 2);
			if (items.length == 2)
				result.put(items[0], items[1]);
		}
		return result;
	}

	@Override
	public boolean handleDoubleClick(ITestElement ctx) {
		if (ctx instanceof TestElement && ((TestElement) ctx).isComparisonFailure()) {
			FailureCompareEditorInput inp = new FailureCompareEditorInput(ctx);
			CompareUI.openCompareEditor(inp);
			return true;
		}
		return false;
	}

	@Override
	public String getSimpleLabel(ITestElement element) {
		if (element instanceof ITestCaseElement) {
			String name = ((ITestCaseElement) element).getTestMethodName();
			Map<String, String> parsed = parseName(name);
			String title = parsed.get("title");
			if (!Strings.isEmpty(title))
				return title;
			return name;
		}
		if (element instanceof ITestSuiteElement)
			return ((ITestSuiteElement) element).getSuiteTypeName();
		return "unknown type:" + element.getClass();
	}

}
