package cn.emay.store.redis;

public class TestBean {
	private String key;

	public TestBean(String key) {
		this.setKey(key);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
