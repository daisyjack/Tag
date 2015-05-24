package mengcheng.tag.frontend.support;

/**
 * 
 * @author Luffy
 * @instructions all Fragment must implement this interface
 */
public interface ReloadFragmentContent {

	/**
	 * currentTag=getActivity.getCurrentTag,
	 * if currentTag!=the currentTag in your Fragment,then reload the sonTag list
	 */
	public void reload();
	
}
