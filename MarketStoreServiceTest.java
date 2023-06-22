	/*
	 * Service实现类
	 */
package com.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cms.entity.marketstore.MarketStoreBean;
import com.cms.form.marketstore.MarketStoreForm;
import com.cms.form.marketstore.MarketStoreListForm;
import com.cms.mapper.marketstore.MarketStoreMapper;
import com.cms.service.marketstore.MarketStoreServiceImpl;
import com.exception.BusinessException;

@SuppressWarnings("rawtypes")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketStoreServiceTest {

	@InjectMocks
	private MarketStoreServiceImpl service;
    
    @Mock
	private MarketStoreMapper mapper;
    
    /*
	 * select方法
	 * 传空值，全检索
	 */
	@Test
	public void testselect_001() {
		
    	MarketStoreListForm paramBean1 = new MarketStoreListForm();
    	
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean("1"));
    	
    	//MarketStoreBean paramBean = new MarketStoreBean();
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
    	try {
    		MarketStoreListForm result = service.select(paramBean1);
    		assertEquals("1", result.getResults().get(0).getStoreId());
    		assertEquals("社員1", result.getResults().get(0).getAddress());
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * select方法
	 * 模拟数据库不存在，抛出异常信息
	 */
	@Test
	public void testselect_002() {
    	
    	MarketStoreBean paramBean = new MarketStoreBean();
    	Mockito.when(this.mapper.select(paramBean)).thenReturn(null);
    	
		try {
			
	    	MarketStoreListForm paramForm = new MarketStoreListForm();
	    	paramForm.setStoreName("1");
	    	paramForm.setAddress("2");
			service.select(paramForm);
			
			System.out.println("如果到这里失败1");
						
		} catch (BusinessException a) {
			assertEquals("検索結果はありません。", a.getMessage());

		}
		catch (Exception e) {
			System.out.println("如果到这里失败2");
			//assertEquals("検索結果はありません。", e.getMessage());
		}
    }
	
	/*
	 * select方法
	 * 传值
	 * 正常返回form
	 */
	@Test
	public void testselect_003() {
    	
    	List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean("1"));
    	
    	//MarketStoreBean paramBean = new MarketStoreBean();
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
    	
		try {
			
	    	MarketStoreListForm paramForm = new MarketStoreListForm();
	    	paramForm.setStoreName("1");
	    	paramForm.setAddress("2");
			MarketStoreListForm paramForm1 = service.select(paramForm);
						
    		assertEquals("社員1",paramForm1.getResults().get(0).getStoreName());
    		assertEquals("1",paramForm1.getResults().get(0).getAddress());

    		//assertEquals(paramForm.getAddress(),paramForm1.getResults().get(0).getAddress());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * insert方法
	 * 传值，正常运行
	 */
	@Test
	public void testinsert_004() {
		MarketStoreForm form = new MarketStoreForm();
		form.setStoreName("11");
		form.setAddress("111");
		form.setPhone("1111");
		form.setStartDay("2022-03-01");
		form.setFinishDay("2022-03-01");
		 
		Mockito.when(this.mapper.selectMaxId()).thenReturn("1");
		
		service.insert(form);
		
		ArgumentCaptor<MarketStoreBean> a = ArgumentCaptor.forClass(MarketStoreBean.class);
		Mockito.verify(mapper).insert(a.capture());
		MarketStoreBean bean = a.getValue();
		
		assertEquals("11",bean.getStoreName());
		assertEquals("111",bean.getAddress());

    }
	
	/*
	 * editInit方法
	 * 传值
	 * 正常返回form
	 */
	@Test
	public void testeditInit_005() {
		MarketStoreForm form = new MarketStoreForm();
		form.setStoreId("1");
		
		List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean("1"));
    	
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
    	
    	MarketStoreForm form1 = service.editInit(form);
		
    	assertEquals("1",form1.getStoreId());
    	assertEquals("社員1",form1.getStoreName());
    	
	}
	
	/*
	 * update方法
	 * 传值
	 * 正常返回form
	 */
	@Test
	public void testupdate_006() {
		MarketStoreForm form = new MarketStoreForm();
		form.setStoreId("1");
		
		List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean("1"));
		Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
		
		service.update(form);
		
		ArgumentCaptor<MarketStoreBean> a = ArgumentCaptor.forClass(MarketStoreBean.class);
		Mockito.verify(mapper).update(a.capture());
		MarketStoreBean bean = a.getValue();
		
		assertEquals("1",bean.getStoreId());
	}
	
	/*
	 * delete方法
	 * 传值
	 */
	@Test
	public void testdelete_007() {
		String id = "1";
		service.delete(id);
		
		ArgumentCaptor<MarketStoreBean> a = ArgumentCaptor.forClass(MarketStoreBean.class);
		Mockito.verify(mapper).delete(a.capture());
		
		MarketStoreBean bean = a.getValue();
		
		assertEquals("1",bean.getStoreId());
	}
	
	/*
	 * deleteAll方法
	 * 传值
	 */
	@Test
	public void testdeleteAll_008() {
		String id = "1,1,1,1";
		String[] velu = id.split(",");
		service.deleteAll(id);
		
		ArgumentCaptor<String[]> a = ArgumentCaptor.forClass(String[].class);
		Mockito.verify(mapper).deleteAll(a.capture());
		
		String[] delatid = a.getValue();
		
		assertArrayEquals(velu,delatid);
		
	}
	
	/*
	 * readInit方法
	 * 传值
	 * 正常返回form
	 */
	@Test
	public void testreadInit_009() {
		MarketStoreForm form = new MarketStoreForm();
		form.setStoreId("1");
		
		List<MarketStoreBean> mockResults = new ArrayList<MarketStoreBean>();
		mockResults.add(createBean("1"));
    	
    	Mockito.when(this.mapper.select(any())).thenReturn(mockResults);
    	
    	MarketStoreForm form1 = service.readInit(form);
		
    	assertEquals("1",form1.getStoreId());
    	assertEquals("社員1",form1.getStoreName());
	
	}
	
	
    private MarketStoreBean createBean(String key) {
    	MarketStoreBean mockBean = new MarketStoreBean();
    	mockBean.setStoreId(key);
    	mockBean.setStoreName("社員"+key);
    	
    	return mockBean;
    }
    
}