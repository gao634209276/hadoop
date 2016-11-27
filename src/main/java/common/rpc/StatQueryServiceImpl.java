package common.rpc;
/*public class StatQueryServiceImpl implements StatQueryService.Iface {

	public Map<String, String> queryDayKPI(String beginDate, String endDate, String kpiCode) throws TException {
		return null;
	}

	public Map<String, String> queryConditionDayKPI(String beginDate, String endDate, String kpiCode, String userName,
	                                                int areaId, String type, String fashion) throws TException {
		Map<String, String> res = new HashMap<String, String>();
		ReportParam param = new ReportParam();
		param.setBeginDate(beginDate + "");
		param.setEndDate(endDate + "");
		param.setKpiCode(kpiCode);
		param.setUserName(userName == "" ? null : userName);
		param.setDistrictId(areaId < 0 ? 0 : areaId);
		param.setProductStyle(fashion == "" ? null : fashion);
		param.setCustomerProperty(type == "" ? null : type);
		List<ReportResult> chart = ((KpiDao) MapperFactory.createMapper(KpiDao.class)).getChartAmount(param);
		Map<String, Integer> title = ((KpiDao) MapperFactory.createMapper(KpiDao.class)).getTitleAmount(param);
		List<Map<String, Integer>> tableAmount = ((KpiDao) MapperFactory.createMapper(KpiDao.class))
				.getTableAmount(param);
		String avgTime = kpiCode.split("_")[0];
		param.setKpiCode(avgTime + "_avg_time");
		List<Map<String, Integer>> tableAvgTime = ((KpiDao) MapperFactory.createMapper(KpiDao.class))
				.getTableAmount(param);
		res.put(ConfigureAPI.RESMAPKEY.CHART, chart.toString());
		res.put(ConfigureAPI.RESMAPKEY.TITLE, title.toString());
		res.put(ConfigureAPI.RESMAPKEY.TABLEAMOUNT, tableAmount.toString());
		res.put(ConfigureAPI.RESMAPKEY.TABLEAVG, tableAvgTime.toString());
		return res;
	}

	public Map<String, String> queryDetail(String beginDate, String endDate, String userName) throws TException {
		return null;
	}

}*/
