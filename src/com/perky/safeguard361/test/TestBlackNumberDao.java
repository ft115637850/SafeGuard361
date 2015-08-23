package com.perky.safeguard361.test;

import java.util.List;
import java.util.Random;

import com.perky.safeguard361.db.dao.BlackNumberDao;
import com.perky.safeguard361.domain.BlackNumberInfo;

import android.content.Context;
import android.test.AndroidTestCase;

public class TestBlackNumberDao extends AndroidTestCase {
	private Context ctx;
	@Override
	protected void setUp() throws Exception {
		ctx= getContext();
		super.setUp();
	}

	/**
	 * ≤‚ ‘ÃÌº”
	 * @throws Exception
	 */
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(ctx);
		Random random = new Random(8979);
		for(long i=1;i<200;i++){
			long number = 13500000000l+i;
			dao.add(number+"", String.valueOf(random.nextInt(3)+1));
		}
//		boolean result = dao.add("13500000000", "1");
//		assertEquals(true, result);
	}
	/**
	 * ≤‚ ‘…æ≥˝
	 * @throws Exception
	 */
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(ctx);
		boolean result = dao.delete("13500000001");
		assertEquals(true, result);
	}
	/**
	 * ≤‚ ‘–ﬁ∏ƒ
	 * @throws Exception
	 */
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(ctx);
		boolean result = dao.changeBlockMode("13500000002", "2");
		assertEquals(true, result);
	}
	/**
	 * ≤‚ ‘≤È—Ø
	 * @throws Exception
	 */
	public void testFind() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(ctx);
		String mode = dao.findBlockMode("13500000000");
		System.out.println(mode);
	}
	/**
	 * ≤‚ ‘≤È—Ø»´≤ø
	 * @throws Exception
	 */
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(ctx);
		List<BlackNumberInfo> infos = dao.findAll();
		for(BlackNumberInfo info: infos){
			System.out.println(info.getNumber()+"---"+info.getMode());
		}
	}
}
