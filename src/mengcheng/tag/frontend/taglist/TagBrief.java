package mengcheng.tag.frontend.taglist;

import java.util.ArrayList;

public class TagBrief {
	private int tag_id;
	private int type;
	private String title;
	private String content;
	private ArrayList<String> url;
	private boolean authentic;
	private String author;
	private MDate create_at;
	private MDate update_at;
	private String parentTag;

	public TagBrief() {
	}

	public TagBrief(int tag_id, int type, String title, String content,
			ArrayList<String> url, String author, boolean authentic,
			MDate create_at, MDate update_at) {
		// TODO Auto-generated constructor stub
		this.tag_id = tag_id;
		this.type = type;
		this.title = title;
		this.content = content;
		this.url = url;
		this.author = author;
		this.authentic = authentic;
		this.create_at = create_at;
		this.update_at = update_at;
	}

	public void setTagID(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getTagID() {
		return tag_id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setUrl(ArrayList<String> url) {
		this.url = url;
	}

	public ArrayList<String> getUrl() {
		return url;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthentic(boolean authentic) {
		this.authentic = authentic;
	}

	public boolean getAuthentic() {
		return authentic;
	}

	public void setCreateTime(MDate create_at) {
		this.create_at = create_at;
	}

	public MDate getCreateTime() {
		return create_at;
	}

	public void setUpdateTime(MDate update_at) {
		this.update_at = update_at;
	}

	public MDate getUpdateTime() {
		return update_at;
	}

	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}

	public String getParentTag() {
		return parentTag;
	}

}
