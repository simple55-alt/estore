package cn.njxzc.estore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.njxzc.estore.dao.IItemDao;
import cn.njxzc.estore.dao.IItemDetailDao;
import cn.njxzc.estore.entity.Item;
import cn.njxzc.estore.entity.ItemDetail;
import cn.njxzc.estore.service.IItemService;

@Service(value = "itemService")
@Transactional
public class ItemServiceImpl implements IItemService {
	
	@Autowired
	private IItemDao itemDao;
	
	@Autowired
	private IItemDetailDao itemDetailDao;
	
	@Override
	public List<Item> findItems() {
		// TODO Auto-generated method stub
		return itemDao.findAll();
	}
	
	@Override
	public List<Item> findHotItems() {
		// TODO Auto-generated method stub
		return itemDao.findHotItems();
	}

	@Override
	public ItemDetail findDetailById(long id) {
		// TODO Auto-generated method stub
		return itemDetailDao.findById(id);
	}

	@Override
	public Item findItemById(long id) {
		// TODO Auto-generated method stub
		return itemDao.findItemById(id);
	}

	@Override
	public Page<Item> findAllByPage(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNo, pageSize);
		return itemDao.findAllByPage();
	}
	
}