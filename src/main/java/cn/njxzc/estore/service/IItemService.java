package cn.njxzc.estore.service;

import java.util.List;

import com.github.pagehelper.Page;

import cn.njxzc.estore.entity.Item;
import cn.njxzc.estore.entity.ItemDetail;

public interface IItemService {
	
	public List<Item> findItems();
	
	/**
	 * 获取12个热搜图书，展现在首页
	 * @return
	 */
	public List<Item> findHotItems();
	
	/**
	 * 通过 id 查询商品详情
	 * @param id
	 */
	public ItemDetail findDetailById(long id);
	
	public Item findItemById(long id);
	
	/**
	 * 分页查询商品信息
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Item> findAllByPage(int pageNo, int pageSize);
	
}
