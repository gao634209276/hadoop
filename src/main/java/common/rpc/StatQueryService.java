package common.rpc;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * git@gitlab.com:dengjie/Resource.git
 */
public class StatQueryService {

	public interface Iface {

		public Map<String, String> queryDayKPI(String beginDate, String endDate, String kpiCode)
				throws org.apache.thrift.TException;

		public Map<String, String> queryConditionDayKPI(String beginDate, String endDate, String kpiCode,
		                                                String userName, int areaId, String type, String fashion) throws org.apache.thrift.TException;

		public Map<String, String> queryDetail(String beginDate, String endDate, String userName)
				throws org.apache.thrift.TException;

	}

	public interface AsyncIface {

		public void queryDayKPI(String beginDate, String endDate, String kpiCode,
		                        org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void queryConditionDayKPI(String beginDate, String endDate, String kpiCode, String userName, int areaId,
		                                 String type, String fashion, org.apache.thrift.async.AsyncMethodCallback resultHandler)
				throws org.apache.thrift.TException;

		public void queryDetail(String beginDate, String endDate, String userName,
		                        org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

	}

	public static class Client extends org.apache.thrift.TServiceClient implements Iface {
		public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
			public Factory() {
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
				return new Client(prot);
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol iprot,
			                        org.apache.thrift.protocol.TProtocol oprot) {
				return new Client(iprot, oprot);
			}
		}

		public Client(org.apache.thrift.protocol.TProtocol prot) {
			super(prot, prot);
		}

		public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
			super(iprot, oprot);
		}

		public Map<String, String> queryDayKPI(String beginDate, String endDate, String kpiCode)
				throws org.apache.thrift.TException {
			send_queryDayKPI(beginDate, endDate, kpiCode);
			return recv_queryDayKPI();
		}

		public void send_queryDayKPI(String beginDate, String endDate, String kpiCode)
				throws org.apache.thrift.TException {
			queryDayKPI_args args = new queryDayKPI_args();
			args.setBeginDate(beginDate);
			args.setEndDate(endDate);
			args.setKpiCode(kpiCode);
			sendBase("queryDayKPI", args);
		}

		public Map<String, String> recv_queryDayKPI() throws org.apache.thrift.TException {
			queryDayKPI_result result = new queryDayKPI_result();
			receiveBase(result, "queryDayKPI");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT,
					"queryDayKPI failed: unknown result");
		}

		public Map<String, String> queryConditionDayKPI(String beginDate, String endDate, String kpiCode,
		                                                String userName, int areaId, String type, String fashion) throws org.apache.thrift.TException {
			send_queryConditionDayKPI(beginDate, endDate, kpiCode, userName, areaId, type, fashion);
			return recv_queryConditionDayKPI();
		}

		public void send_queryConditionDayKPI(String beginDate, String endDate, String kpiCode, String userName,
		                                      int areaId, String type, String fashion) throws org.apache.thrift.TException {
			queryConditionDayKPI_args args = new queryConditionDayKPI_args();
			args.setBeginDate(beginDate);
			args.setEndDate(endDate);
			args.setKpiCode(kpiCode);
			args.setUserName(userName);
			args.setAreaId(areaId);
			args.setType(type);
			args.setFashion(fashion);
			sendBase("queryConditionDayKPI", args);
		}

		public Map<String, String> recv_queryConditionDayKPI() throws org.apache.thrift.TException {
			queryConditionDayKPI_result result = new queryConditionDayKPI_result();
			receiveBase(result, "queryConditionDayKPI");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT,
					"queryConditionDayKPI failed: unknown result");
		}

		public Map<String, String> queryDetail(String beginDate, String endDate, String userName)
				throws org.apache.thrift.TException {
			send_queryDetail(beginDate, endDate, userName);
			return recv_queryDetail();
		}

		public void send_queryDetail(String beginDate, String endDate, String userName)
				throws org.apache.thrift.TException {
			queryDetail_args args = new queryDetail_args();
			args.setBeginDate(beginDate);
			args.setEndDate(endDate);
			args.setUserName(userName);
			sendBase("queryDetail", args);
		}

		public Map<String, String> recv_queryDetail() throws org.apache.thrift.TException {
			queryDetail_result result = new queryDetail_result();
			receiveBase(result, "queryDetail");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT,
					"queryDetail failed: unknown result");
		}

	}

	public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
		public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
			private org.apache.thrift.async.TAsyncClientManager clientManager;
			private org.apache.thrift.protocol.TProtocolFactory protocolFactory;

			public Factory(org.apache.thrift.async.TAsyncClientManager clientManager,
			               org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
				this.clientManager = clientManager;
				this.protocolFactory = protocolFactory;
			}

			public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
				return new AsyncClient(protocolFactory, clientManager, transport);
			}
		}

		public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory,
		                   org.apache.thrift.async.TAsyncClientManager clientManager,
		                   org.apache.thrift.transport.TNonblockingTransport transport) {
			super(protocolFactory, clientManager, transport);
		}

		public void queryDayKPI(String beginDate, String endDate, String kpiCode,
		                        org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			queryDayKPI_call method_call = new queryDayKPI_call(beginDate, endDate, kpiCode, resultHandler, this,
					___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class queryDayKPI_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String beginDate;
			private String endDate;
			private String kpiCode;

			public queryDayKPI_call(String beginDate, String endDate, String kpiCode,
			                        org.apache.thrift.async.AsyncMethodCallback resultHandler,
			                        org.apache.thrift.async.TAsyncClient client,
			                        org.apache.thrift.protocol.TProtocolFactory protocolFactory,
			                        org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.beginDate = beginDate;
				this.endDate = endDate;
				this.kpiCode = kpiCode;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("queryDayKPI",
						org.apache.thrift.protocol.TMessageType.CALL, 0));
				queryDayKPI_args args = new queryDayKPI_args();
				args.setBeginDate(beginDate);
				args.setEndDate(endDate);
				args.setKpiCode(kpiCode);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public Map<String, String> getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(
						getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_queryDayKPI();
			}
		}

		public void queryConditionDayKPI(String beginDate, String endDate, String kpiCode, String userName, int areaId,
		                                 String type, String fashion, org.apache.thrift.async.AsyncMethodCallback resultHandler)
				throws org.apache.thrift.TException {
			checkReady();
			queryConditionDayKPI_call method_call = new queryConditionDayKPI_call(beginDate, endDate, kpiCode,
					userName, areaId, type, fashion, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class queryConditionDayKPI_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String beginDate;
			private String endDate;
			private String kpiCode;
			private String userName;
			private int areaId;
			private String type;
			private String fashion;

			public queryConditionDayKPI_call(String beginDate, String endDate, String kpiCode, String userName,
			                                 int areaId, String type, String fashion, org.apache.thrift.async.AsyncMethodCallback resultHandler,
			                                 org.apache.thrift.async.TAsyncClient client,
			                                 org.apache.thrift.protocol.TProtocolFactory protocolFactory,
			                                 org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.beginDate = beginDate;
				this.endDate = endDate;
				this.kpiCode = kpiCode;
				this.userName = userName;
				this.areaId = areaId;
				this.type = type;
				this.fashion = fashion;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("queryConditionDayKPI",
						org.apache.thrift.protocol.TMessageType.CALL, 0));
				queryConditionDayKPI_args args = new queryConditionDayKPI_args();
				args.setBeginDate(beginDate);
				args.setEndDate(endDate);
				args.setKpiCode(kpiCode);
				args.setUserName(userName);
				args.setAreaId(areaId);
				args.setType(type);
				args.setFashion(fashion);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public Map<String, String> getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(
						getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_queryConditionDayKPI();
			}
		}

		public void queryDetail(String beginDate, String endDate, String userName,
		                        org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			queryDetail_call method_call = new queryDetail_call(beginDate, endDate, userName, resultHandler, this,
					___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class queryDetail_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String beginDate;
			private String endDate;
			private String userName;

			public queryDetail_call(String beginDate, String endDate, String userName,
			                        org.apache.thrift.async.AsyncMethodCallback resultHandler,
			                        org.apache.thrift.async.TAsyncClient client,
			                        org.apache.thrift.protocol.TProtocolFactory protocolFactory,
			                        org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.beginDate = beginDate;
				this.endDate = endDate;
				this.userName = userName;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("queryDetail",
						org.apache.thrift.protocol.TMessageType.CALL, 0));
				queryDetail_args args = new queryDetail_args();
				args.setBeginDate(beginDate);
				args.setEndDate(endDate);
				args.setUserName(userName);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public Map<String, String> getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(
						getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_queryDetail();
			}
		}

	}

	public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements
			org.apache.thrift.TProcessor {
		private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

		public Processor(I iface) {
			super(
					iface,
					getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
		}

		protected Processor(I iface,
		                    Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			super(iface, getProcessMap(processMap));
		}

		private static <I extends Iface> Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(
				Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			processMap.put("queryDayKPI", new queryDayKPI());
			processMap.put("queryConditionDayKPI", new queryConditionDayKPI());
			processMap.put("queryDetail", new queryDetail());
			return processMap;
		}

		public static class queryDayKPI<I extends Iface> extends org.apache.thrift.ProcessFunction<I, queryDayKPI_args> {
			public queryDayKPI() {
				super("queryDayKPI");
			}

			public queryDayKPI_args getEmptyArgsInstance() {
				return new queryDayKPI_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public queryDayKPI_result getResult(I iface, queryDayKPI_args args) throws org.apache.thrift.TException {
				queryDayKPI_result result = new queryDayKPI_result();
				result.success = iface.queryDayKPI(args.beginDate, args.endDate, args.kpiCode);
				return result;
			}
		}

		public static class queryConditionDayKPI<I extends Iface> extends
				org.apache.thrift.ProcessFunction<I, queryConditionDayKPI_args> {
			public queryConditionDayKPI() {
				super("queryConditionDayKPI");
			}

			public queryConditionDayKPI_args getEmptyArgsInstance() {
				return new queryConditionDayKPI_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public queryConditionDayKPI_result getResult(I iface, queryConditionDayKPI_args args)
					throws org.apache.thrift.TException {
				queryConditionDayKPI_result result = new queryConditionDayKPI_result();
				result.success = iface.queryConditionDayKPI(args.beginDate, args.endDate, args.kpiCode, args.userName,
						args.areaId, args.type, args.fashion);
				return result;
			}
		}

		public static class queryDetail<I extends Iface> extends org.apache.thrift.ProcessFunction<I, queryDetail_args> {
			public queryDetail() {
				super("queryDetail");
			}

			public queryDetail_args getEmptyArgsInstance() {
				return new queryDetail_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public queryDetail_result getResult(I iface, queryDetail_args args) throws org.apache.thrift.TException {
				queryDetail_result result = new queryDetail_result();
				result.success = iface.queryDetail(args.beginDate, args.endDate, args.userName);
				return result;
			}
		}

	}

	public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
		private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class.getName());

		public AsyncProcessor(I iface) {
			super(
					iface,
					getProcessMap(new HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
		}

		protected AsyncProcessor(I iface,
		                         Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
			super(iface, getProcessMap(processMap));
		}

		private static <I extends AsyncIface> Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> getProcessMap(
				Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
			processMap.put("queryDayKPI", new queryDayKPI());
			processMap.put("queryConditionDayKPI", new queryConditionDayKPI());
			processMap.put("queryDetail", new queryDetail());
			return processMap;
		}

		public static class queryDayKPI<I extends AsyncIface> extends
				org.apache.thrift.AsyncProcessFunction<I, queryDayKPI_args, Map<String, String>> {
			public queryDayKPI() {
				super("queryDayKPI");
			}

			public queryDayKPI_args getEmptyArgsInstance() {
				return new queryDayKPI_args();
			}

			public AsyncMethodCallback<Map<String, String>> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Map<String, String>>() {
					public void onComplete(Map<String, String> o) {
						queryDayKPI_result result = new queryDayKPI_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						queryDayKPI_result result = new queryDayKPI_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(
									org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, queryDayKPI_args args,
			                  org.apache.thrift.async.AsyncMethodCallback<Map<String, String>> resultHandler) throws TException {
				iface.queryDayKPI(args.beginDate, args.endDate, args.kpiCode, resultHandler);
			}
		}

		public static class queryConditionDayKPI<I extends AsyncIface> extends
				org.apache.thrift.AsyncProcessFunction<I, queryConditionDayKPI_args, Map<String, String>> {
			public queryConditionDayKPI() {
				super("queryConditionDayKPI");
			}

			public queryConditionDayKPI_args getEmptyArgsInstance() {
				return new queryConditionDayKPI_args();
			}

			public AsyncMethodCallback<Map<String, String>> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Map<String, String>>() {
					public void onComplete(Map<String, String> o) {
						queryConditionDayKPI_result result = new queryConditionDayKPI_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						queryConditionDayKPI_result result = new queryConditionDayKPI_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(
									org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, queryConditionDayKPI_args args,
			                  org.apache.thrift.async.AsyncMethodCallback<Map<String, String>> resultHandler) throws TException {
				iface.queryConditionDayKPI(args.beginDate, args.endDate, args.kpiCode, args.userName, args.areaId,
						args.type, args.fashion, resultHandler);
			}
		}

		public static class queryDetail<I extends AsyncIface> extends
				org.apache.thrift.AsyncProcessFunction<I, queryDetail_args, Map<String, String>> {
			public queryDetail() {
				super("queryDetail");
			}

			public queryDetail_args getEmptyArgsInstance() {
				return new queryDetail_args();
			}

			public AsyncMethodCallback<Map<String, String>> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Map<String, String>>() {
					public void onComplete(Map<String, String> o) {
						queryDetail_result result = new queryDetail_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						queryDetail_result result = new queryDetail_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(
									org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, queryDetail_args args,
			                  org.apache.thrift.async.AsyncMethodCallback<Map<String, String>> resultHandler) throws TException {
				iface.queryDetail(args.beginDate, args.endDate, args.userName, resultHandler);
			}
		}

	}

	public static class queryDayKPI_args implements
			org.apache.thrift.TBase<queryDayKPI_args, queryDayKPI_args._Fields>, java.io.Serializable, Cloneable,
			Comparable<queryDayKPI_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryDayKPI_args");

		private static final org.apache.thrift.protocol.TField BEGIN_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"beginDate", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField END_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"endDate", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField KPI_CODE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"kpiCode", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryDayKPI_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryDayKPI_argsTupleSchemeFactory());
		}

		public String beginDate; // required
		public String endDate; // required
		public String kpiCode; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			BEGIN_DATE((short) 1, "beginDate"), END_DATE((short) 2, "endDate"), KPI_CODE((short) 3, "kpiCode");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 1: // BEGIN_DATE
						return BEGIN_DATE;
					case 2: // END_DATE
						return END_DATE;
					case 3: // KPI_CODE
						return KPI_CODE;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.BEGIN_DATE, new org.apache.thrift.meta_data.FieldMetaData("beginDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.END_DATE, new org.apache.thrift.meta_data.FieldMetaData("endDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.KPI_CODE, new org.apache.thrift.meta_data.FieldMetaData("kpiCode",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(queryDayKPI_args.class, metaDataMap);
		}

		public queryDayKPI_args() {
		}

		public queryDayKPI_args(String beginDate, String endDate, String kpiCode) {
			this();
			this.beginDate = beginDate;
			this.endDate = endDate;
			this.kpiCode = kpiCode;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryDayKPI_args(queryDayKPI_args other) {
			if (other.isSetBeginDate()) {
				this.beginDate = other.beginDate;
			}
			if (other.isSetEndDate()) {
				this.endDate = other.endDate;
			}
			if (other.isSetKpiCode()) {
				this.kpiCode = other.kpiCode;
			}
		}

		public queryDayKPI_args deepCopy() {
			return new queryDayKPI_args(this);
		}

		public void clear() {
			this.beginDate = null;
			this.endDate = null;
			this.kpiCode = null;
		}

		public String getBeginDate() {
			return this.beginDate;
		}

		public queryDayKPI_args setBeginDate(String beginDate) {
			this.beginDate = beginDate;
			return this;
		}

		public void unsetBeginDate() {
			this.beginDate = null;
		}

		/**
		 * Returns true if field beginDate is set (has been assigned a value)
		 * and false otherwise
		 */
		public boolean isSetBeginDate() {
			return this.beginDate != null;
		}

		public void setBeginDateIsSet(boolean value) {
			if (!value) {
				this.beginDate = null;
			}
		}

		public String getEndDate() {
			return this.endDate;
		}

		public queryDayKPI_args setEndDate(String endDate) {
			this.endDate = endDate;
			return this;
		}

		public void unsetEndDate() {
			this.endDate = null;
		}

		/**
		 * Returns true if field endDate is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetEndDate() {
			return this.endDate != null;
		}

		public void setEndDateIsSet(boolean value) {
			if (!value) {
				this.endDate = null;
			}
		}

		public String getKpiCode() {
			return this.kpiCode;
		}

		public queryDayKPI_args setKpiCode(String kpiCode) {
			this.kpiCode = kpiCode;
			return this;
		}

		public void unsetKpiCode() {
			this.kpiCode = null;
		}

		/**
		 * Returns true if field kpiCode is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetKpiCode() {
			return this.kpiCode != null;
		}

		public void setKpiCodeIsSet(boolean value) {
			if (!value) {
				this.kpiCode = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case BEGIN_DATE:
					if (value == null) {
						unsetBeginDate();
					} else {
						setBeginDate((String) value);
					}
					break;

				case END_DATE:
					if (value == null) {
						unsetEndDate();
					} else {
						setEndDate((String) value);
					}
					break;

				case KPI_CODE:
					if (value == null) {
						unsetKpiCode();
					} else {
						setKpiCode((String) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case BEGIN_DATE:
					return getBeginDate();

				case END_DATE:
					return getEndDate();

				case KPI_CODE:
					return getKpiCode();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case BEGIN_DATE:
					return isSetBeginDate();
				case END_DATE:
					return isSetEndDate();
				case KPI_CODE:
					return isSetKpiCode();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryDayKPI_args)
				return this.equals((queryDayKPI_args) that);
			return false;
		}

		public boolean equals(queryDayKPI_args that) {
			if (that == null)
				return false;

			boolean this_present_beginDate = true && this.isSetBeginDate();
			boolean that_present_beginDate = true && that.isSetBeginDate();
			if (this_present_beginDate || that_present_beginDate) {
				if (!(this_present_beginDate && that_present_beginDate))
					return false;
				if (!this.beginDate.equals(that.beginDate))
					return false;
			}

			boolean this_present_endDate = true && this.isSetEndDate();
			boolean that_present_endDate = true && that.isSetEndDate();
			if (this_present_endDate || that_present_endDate) {
				if (!(this_present_endDate && that_present_endDate))
					return false;
				if (!this.endDate.equals(that.endDate))
					return false;
			}

			boolean this_present_kpiCode = true && this.isSetKpiCode();
			boolean that_present_kpiCode = true && that.isSetKpiCode();
			if (this_present_kpiCode || that_present_kpiCode) {
				if (!(this_present_kpiCode && that_present_kpiCode))
					return false;
				if (!this.kpiCode.equals(that.kpiCode))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryDayKPI_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetBeginDate()).compareTo(other.isSetBeginDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetBeginDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.beginDate, other.beginDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetEndDate()).compareTo(other.isSetEndDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetEndDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.endDate, other.endDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetKpiCode()).compareTo(other.isSetKpiCode());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetKpiCode()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.kpiCode, other.kpiCode);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryDayKPI_args(");
			boolean first = true;

			sb.append("beginDate:");
			if (this.beginDate == null) {
				sb.append("null");
			} else {
				sb.append(this.beginDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("endDate:");
			if (this.endDate == null) {
				sb.append("null");
			} else {
				sb.append(this.endDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("kpiCode:");
			if (this.kpiCode == null) {
				sb.append("null");
			} else {
				sb.append(this.kpiCode);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryDayKPI_argsStandardSchemeFactory implements SchemeFactory {
			public queryDayKPI_argsStandardScheme getScheme() {
				return new queryDayKPI_argsStandardScheme();
			}
		}

		private static class queryDayKPI_argsStandardScheme extends StandardScheme<queryDayKPI_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryDayKPI_args struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 1: // BEGIN_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.beginDate = iprot.readString();
								struct.setBeginDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 2: // END_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.endDate = iprot.readString();
								struct.setEndDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 3: // KPI_CODE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.kpiCode = iprot.readString();
								struct.setKpiCodeIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryDayKPI_args struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.beginDate != null) {
					oprot.writeFieldBegin(BEGIN_DATE_FIELD_DESC);
					oprot.writeString(struct.beginDate);
					oprot.writeFieldEnd();
				}
				if (struct.endDate != null) {
					oprot.writeFieldBegin(END_DATE_FIELD_DESC);
					oprot.writeString(struct.endDate);
					oprot.writeFieldEnd();
				}
				if (struct.kpiCode != null) {
					oprot.writeFieldBegin(KPI_CODE_FIELD_DESC);
					oprot.writeString(struct.kpiCode);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryDayKPI_argsTupleSchemeFactory implements SchemeFactory {
			public queryDayKPI_argsTupleScheme getScheme() {
				return new queryDayKPI_argsTupleScheme();
			}
		}

		private static class queryDayKPI_argsTupleScheme extends TupleScheme<queryDayKPI_args> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryDayKPI_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetBeginDate()) {
					optionals.set(0);
				}
				if (struct.isSetEndDate()) {
					optionals.set(1);
				}
				if (struct.isSetKpiCode()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetBeginDate()) {
					oprot.writeString(struct.beginDate);
				}
				if (struct.isSetEndDate()) {
					oprot.writeString(struct.endDate);
				}
				if (struct.isSetKpiCode()) {
					oprot.writeString(struct.kpiCode);
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryDayKPI_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.beginDate = iprot.readString();
					struct.setBeginDateIsSet(true);
				}
				if (incoming.get(1)) {
					struct.endDate = iprot.readString();
					struct.setEndDateIsSet(true);
				}
				if (incoming.get(2)) {
					struct.kpiCode = iprot.readString();
					struct.setKpiCodeIsSet(true);
				}
			}
		}

	}

	public static class queryDayKPI_result implements
			org.apache.thrift.TBase<queryDayKPI_result, queryDayKPI_result._Fields>, java.io.Serializable, Cloneable,
			Comparable<queryDayKPI_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryDayKPI_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"success", org.apache.thrift.protocol.TType.MAP, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryDayKPI_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryDayKPI_resultTupleSchemeFactory());
		}

		public Map<String, String> success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 0: // SUCCESS
						return SUCCESS;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.SUCCESS,
					new org.apache.thrift.meta_data.FieldMetaData("success",
							org.apache.thrift.TFieldRequirementType.DEFAULT,
							new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP,
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING),
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING))));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(queryDayKPI_result.class, metaDataMap);
		}

		public queryDayKPI_result() {
		}

		public queryDayKPI_result(Map<String, String> success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryDayKPI_result(queryDayKPI_result other) {
			if (other.isSetSuccess()) {
				Map<String, String> __this__success = new HashMap<String, String>(other.success);
				this.success = __this__success;
			}
		}

		public queryDayKPI_result deepCopy() {
			return new queryDayKPI_result(this);
		}

		public void clear() {
			this.success = null;
		}

		public int getSuccessSize() {
			return (this.success == null) ? 0 : this.success.size();
		}

		public void putToSuccess(String key, String val) {
			if (this.success == null) {
				this.success = new HashMap<String, String>();
			}
			this.success.put(key, val);
		}

		public Map<String, String> getSuccess() {
			return this.success;
		}

		public queryDayKPI_result setSuccess(Map<String, String> success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case SUCCESS:
					if (value == null) {
						unsetSuccess();
					} else {
						setSuccess((Map<String, String>) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case SUCCESS:
					return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case SUCCESS:
					return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryDayKPI_result)
				return this.equals((queryDayKPI_result) that);
			return false;
		}

		public boolean equals(queryDayKPI_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryDayKPI_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryDayKPI_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryDayKPI_resultStandardSchemeFactory implements SchemeFactory {
			public queryDayKPI_resultStandardScheme getScheme() {
				return new queryDayKPI_resultStandardScheme();
			}
		}

		private static class queryDayKPI_resultStandardScheme extends StandardScheme<queryDayKPI_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryDayKPI_result struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 0: // SUCCESS
							if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
								{
									org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
									struct.success = new HashMap<String, String>(2 * _map0.size);
									for (int _i1 = 0; _i1 < _map0.size; ++_i1) {
										String _key2;
										String _val3;
										_key2 = iprot.readString();
										_val3 = iprot.readString();
										struct.success.put(_key2, _val3);
									}
									iprot.readMapEnd();
								}
								struct.setSuccessIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryDayKPI_result struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					{
						oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								struct.success.size()));
						for (Map.Entry<String, String> _iter4 : struct.success.entrySet()) {
							oprot.writeString(_iter4.getKey());
							oprot.writeString(_iter4.getValue());
						}
						oprot.writeMapEnd();
					}
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryDayKPI_resultTupleSchemeFactory implements SchemeFactory {
			public queryDayKPI_resultTupleScheme getScheme() {
				return new queryDayKPI_resultTupleScheme();
			}
		}

		private static class queryDayKPI_resultTupleScheme extends TupleScheme<queryDayKPI_result> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryDayKPI_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					{
						oprot.writeI32(struct.success.size());
						for (Map.Entry<String, String> _iter5 : struct.success.entrySet()) {
							oprot.writeString(_iter5.getKey());
							oprot.writeString(_iter5.getValue());
						}
					}
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryDayKPI_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					{
						org.apache.thrift.protocol.TMap _map6 = new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								iprot.readI32());
						struct.success = new HashMap<String, String>(2 * _map6.size);
						for (int _i7 = 0; _i7 < _map6.size; ++_i7) {
							String _key8;
							String _val9;
							_key8 = iprot.readString();
							_val9 = iprot.readString();
							struct.success.put(_key8, _val9);
						}
					}
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class queryConditionDayKPI_args implements
			org.apache.thrift.TBase<queryConditionDayKPI_args, queryConditionDayKPI_args._Fields>,
			java.io.Serializable, Cloneable, Comparable<queryConditionDayKPI_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryConditionDayKPI_args");

		private static final org.apache.thrift.protocol.TField BEGIN_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"beginDate", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField END_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"endDate", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField KPI_CODE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"kpiCode", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"userName", org.apache.thrift.protocol.TType.STRING, (short) 4);
		private static final org.apache.thrift.protocol.TField AREA_ID_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"areaId", org.apache.thrift.protocol.TType.I32, (short) 5);
		private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"type", org.apache.thrift.protocol.TType.STRING, (short) 6);
		private static final org.apache.thrift.protocol.TField FASHION_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"fashion", org.apache.thrift.protocol.TType.STRING, (short) 7);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryConditionDayKPI_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryConditionDayKPI_argsTupleSchemeFactory());
		}

		public String beginDate; // required
		public String endDate; // required
		public String kpiCode; // required
		public String userName; // required
		public int areaId; // required
		public String type; // required
		public String fashion; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			BEGIN_DATE((short) 1, "beginDate"), END_DATE((short) 2, "endDate"), KPI_CODE((short) 3, "kpiCode"), USER_NAME(
					(short) 4, "userName"), AREA_ID((short) 5, "areaId"), TYPE((short) 6, "type"), FASHION((short) 7,
					"fashion");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 1: // BEGIN_DATE
						return BEGIN_DATE;
					case 2: // END_DATE
						return END_DATE;
					case 3: // KPI_CODE
						return KPI_CODE;
					case 4: // USER_NAME
						return USER_NAME;
					case 5: // AREA_ID
						return AREA_ID;
					case 6: // TYPE
						return TYPE;
					case 7: // FASHION
						return FASHION;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __AREAID_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.BEGIN_DATE, new org.apache.thrift.meta_data.FieldMetaData("beginDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.END_DATE, new org.apache.thrift.meta_data.FieldMetaData("endDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.KPI_CODE, new org.apache.thrift.meta_data.FieldMetaData("kpiCode",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("userName",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.AREA_ID, new org.apache.thrift.meta_data.FieldMetaData("areaId",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
			tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.FASHION, new org.apache.thrift.meta_data.FieldMetaData("fashion",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData
					.addStructMetaDataMap(queryConditionDayKPI_args.class, metaDataMap);
		}

		public queryConditionDayKPI_args() {
		}

		public queryConditionDayKPI_args(String beginDate, String endDate, String kpiCode, String userName, int areaId,
		                                 String type, String fashion) {
			this();
			this.beginDate = beginDate;
			this.endDate = endDate;
			this.kpiCode = kpiCode;
			this.userName = userName;
			this.areaId = areaId;
			setAreaIdIsSet(true);
			this.type = type;
			this.fashion = fashion;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryConditionDayKPI_args(queryConditionDayKPI_args other) {
			__isset_bitfield = other.__isset_bitfield;
			if (other.isSetBeginDate()) {
				this.beginDate = other.beginDate;
			}
			if (other.isSetEndDate()) {
				this.endDate = other.endDate;
			}
			if (other.isSetKpiCode()) {
				this.kpiCode = other.kpiCode;
			}
			if (other.isSetUserName()) {
				this.userName = other.userName;
			}
			this.areaId = other.areaId;
			if (other.isSetType()) {
				this.type = other.type;
			}
			if (other.isSetFashion()) {
				this.fashion = other.fashion;
			}
		}

		public queryConditionDayKPI_args deepCopy() {
			return new queryConditionDayKPI_args(this);
		}

		public void clear() {
			this.beginDate = null;
			this.endDate = null;
			this.kpiCode = null;
			this.userName = null;
			setAreaIdIsSet(false);
			this.areaId = 0;
			this.type = null;
			this.fashion = null;
		}

		public String getBeginDate() {
			return this.beginDate;
		}

		public queryConditionDayKPI_args setBeginDate(String beginDate) {
			this.beginDate = beginDate;
			return this;
		}

		public void unsetBeginDate() {
			this.beginDate = null;
		}

		/**
		 * Returns true if field beginDate is set (has been assigned a value)
		 * and false otherwise
		 */
		public boolean isSetBeginDate() {
			return this.beginDate != null;
		}

		public void setBeginDateIsSet(boolean value) {
			if (!value) {
				this.beginDate = null;
			}
		}

		public String getEndDate() {
			return this.endDate;
		}

		public queryConditionDayKPI_args setEndDate(String endDate) {
			this.endDate = endDate;
			return this;
		}

		public void unsetEndDate() {
			this.endDate = null;
		}

		/**
		 * Returns true if field endDate is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetEndDate() {
			return this.endDate != null;
		}

		public void setEndDateIsSet(boolean value) {
			if (!value) {
				this.endDate = null;
			}
		}

		public String getKpiCode() {
			return this.kpiCode;
		}

		public queryConditionDayKPI_args setKpiCode(String kpiCode) {
			this.kpiCode = kpiCode;
			return this;
		}

		public void unsetKpiCode() {
			this.kpiCode = null;
		}

		/**
		 * Returns true if field kpiCode is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetKpiCode() {
			return this.kpiCode != null;
		}

		public void setKpiCodeIsSet(boolean value) {
			if (!value) {
				this.kpiCode = null;
			}
		}

		public String getUserName() {
			return this.userName;
		}

		public queryConditionDayKPI_args setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public void unsetUserName() {
			this.userName = null;
		}

		/**
		 * Returns true if field userName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetUserName() {
			return this.userName != null;
		}

		public void setUserNameIsSet(boolean value) {
			if (!value) {
				this.userName = null;
			}
		}

		public int getAreaId() {
			return this.areaId;
		}

		public queryConditionDayKPI_args setAreaId(int areaId) {
			this.areaId = areaId;
			setAreaIdIsSet(true);
			return this;
		}

		public void unsetAreaId() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __AREAID_ISSET_ID);
		}

		/**
		 * Returns true if field areaId is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetAreaId() {
			return EncodingUtils.testBit(__isset_bitfield, __AREAID_ISSET_ID);
		}

		public void setAreaIdIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __AREAID_ISSET_ID, value);
		}

		public String getType() {
			return this.type;
		}

		public queryConditionDayKPI_args setType(String type) {
			this.type = type;
			return this;
		}

		public void unsetType() {
			this.type = null;
		}

		/**
		 * Returns true if field type is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetType() {
			return this.type != null;
		}

		public void setTypeIsSet(boolean value) {
			if (!value) {
				this.type = null;
			}
		}

		public String getFashion() {
			return this.fashion;
		}

		public queryConditionDayKPI_args setFashion(String fashion) {
			this.fashion = fashion;
			return this;
		}

		public void unsetFashion() {
			this.fashion = null;
		}

		/**
		 * Returns true if field fashion is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetFashion() {
			return this.fashion != null;
		}

		public void setFashionIsSet(boolean value) {
			if (!value) {
				this.fashion = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case BEGIN_DATE:
					if (value == null) {
						unsetBeginDate();
					} else {
						setBeginDate((String) value);
					}
					break;

				case END_DATE:
					if (value == null) {
						unsetEndDate();
					} else {
						setEndDate((String) value);
					}
					break;

				case KPI_CODE:
					if (value == null) {
						unsetKpiCode();
					} else {
						setKpiCode((String) value);
					}
					break;

				case USER_NAME:
					if (value == null) {
						unsetUserName();
					} else {
						setUserName((String) value);
					}
					break;

				case AREA_ID:
					if (value == null) {
						unsetAreaId();
					} else {
						setAreaId((Integer) value);
					}
					break;

				case TYPE:
					if (value == null) {
						unsetType();
					} else {
						setType((String) value);
					}
					break;

				case FASHION:
					if (value == null) {
						unsetFashion();
					} else {
						setFashion((String) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case BEGIN_DATE:
					return getBeginDate();

				case END_DATE:
					return getEndDate();

				case KPI_CODE:
					return getKpiCode();

				case USER_NAME:
					return getUserName();

				case AREA_ID:
					return Integer.valueOf(getAreaId());

				case TYPE:
					return getType();

				case FASHION:
					return getFashion();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case BEGIN_DATE:
					return isSetBeginDate();
				case END_DATE:
					return isSetEndDate();
				case KPI_CODE:
					return isSetKpiCode();
				case USER_NAME:
					return isSetUserName();
				case AREA_ID:
					return isSetAreaId();
				case TYPE:
					return isSetType();
				case FASHION:
					return isSetFashion();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryConditionDayKPI_args)
				return this.equals((queryConditionDayKPI_args) that);
			return false;
		}

		public boolean equals(queryConditionDayKPI_args that) {
			if (that == null)
				return false;

			boolean this_present_beginDate = true && this.isSetBeginDate();
			boolean that_present_beginDate = true && that.isSetBeginDate();
			if (this_present_beginDate || that_present_beginDate) {
				if (!(this_present_beginDate && that_present_beginDate))
					return false;
				if (!this.beginDate.equals(that.beginDate))
					return false;
			}

			boolean this_present_endDate = true && this.isSetEndDate();
			boolean that_present_endDate = true && that.isSetEndDate();
			if (this_present_endDate || that_present_endDate) {
				if (!(this_present_endDate && that_present_endDate))
					return false;
				if (!this.endDate.equals(that.endDate))
					return false;
			}

			boolean this_present_kpiCode = true && this.isSetKpiCode();
			boolean that_present_kpiCode = true && that.isSetKpiCode();
			if (this_present_kpiCode || that_present_kpiCode) {
				if (!(this_present_kpiCode && that_present_kpiCode))
					return false;
				if (!this.kpiCode.equals(that.kpiCode))
					return false;
			}

			boolean this_present_userName = true && this.isSetUserName();
			boolean that_present_userName = true && that.isSetUserName();
			if (this_present_userName || that_present_userName) {
				if (!(this_present_userName && that_present_userName))
					return false;
				if (!this.userName.equals(that.userName))
					return false;
			}

			boolean this_present_areaId = true;
			boolean that_present_areaId = true;
			if (this_present_areaId || that_present_areaId) {
				if (!(this_present_areaId && that_present_areaId))
					return false;
				if (this.areaId != that.areaId)
					return false;
			}

			boolean this_present_type = true && this.isSetType();
			boolean that_present_type = true && that.isSetType();
			if (this_present_type || that_present_type) {
				if (!(this_present_type && that_present_type))
					return false;
				if (!this.type.equals(that.type))
					return false;
			}

			boolean this_present_fashion = true && this.isSetFashion();
			boolean that_present_fashion = true && that.isSetFashion();
			if (this_present_fashion || that_present_fashion) {
				if (!(this_present_fashion && that_present_fashion))
					return false;
				if (!this.fashion.equals(that.fashion))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryConditionDayKPI_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetBeginDate()).compareTo(other.isSetBeginDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetBeginDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.beginDate, other.beginDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetEndDate()).compareTo(other.isSetEndDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetEndDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.endDate, other.endDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetKpiCode()).compareTo(other.isSetKpiCode());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetKpiCode()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.kpiCode, other.kpiCode);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetUserName()).compareTo(other.isSetUserName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetUserName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userName, other.userName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetAreaId()).compareTo(other.isSetAreaId());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetAreaId()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.areaId, other.areaId);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetType()).compareTo(other.isSetType());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetType()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, other.type);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetFashion()).compareTo(other.isSetFashion());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetFashion()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fashion, other.fashion);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryConditionDayKPI_args(");
			boolean first = true;

			sb.append("beginDate:");
			if (this.beginDate == null) {
				sb.append("null");
			} else {
				sb.append(this.beginDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("endDate:");
			if (this.endDate == null) {
				sb.append("null");
			} else {
				sb.append(this.endDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("kpiCode:");
			if (this.kpiCode == null) {
				sb.append("null");
			} else {
				sb.append(this.kpiCode);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("userName:");
			if (this.userName == null) {
				sb.append("null");
			} else {
				sb.append(this.userName);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("areaId:");
			sb.append(this.areaId);
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("type:");
			if (this.type == null) {
				sb.append("null");
			} else {
				sb.append(this.type);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("fashion:");
			if (this.fashion == null) {
				sb.append("null");
			} else {
				sb.append(this.fashion);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryConditionDayKPI_argsStandardSchemeFactory implements SchemeFactory {
			public queryConditionDayKPI_argsStandardScheme getScheme() {
				return new queryConditionDayKPI_argsStandardScheme();
			}
		}

		private static class queryConditionDayKPI_argsStandardScheme extends StandardScheme<queryConditionDayKPI_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryConditionDayKPI_args struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 1: // BEGIN_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.beginDate = iprot.readString();
								struct.setBeginDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 2: // END_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.endDate = iprot.readString();
								struct.setEndDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 3: // KPI_CODE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.kpiCode = iprot.readString();
								struct.setKpiCodeIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 4: // USER_NAME
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.userName = iprot.readString();
								struct.setUserNameIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 5: // AREA_ID
							if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
								struct.areaId = iprot.readI32();
								struct.setAreaIdIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 6: // TYPE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.type = iprot.readString();
								struct.setTypeIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 7: // FASHION
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.fashion = iprot.readString();
								struct.setFashionIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryConditionDayKPI_args struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.beginDate != null) {
					oprot.writeFieldBegin(BEGIN_DATE_FIELD_DESC);
					oprot.writeString(struct.beginDate);
					oprot.writeFieldEnd();
				}
				if (struct.endDate != null) {
					oprot.writeFieldBegin(END_DATE_FIELD_DESC);
					oprot.writeString(struct.endDate);
					oprot.writeFieldEnd();
				}
				if (struct.kpiCode != null) {
					oprot.writeFieldBegin(KPI_CODE_FIELD_DESC);
					oprot.writeString(struct.kpiCode);
					oprot.writeFieldEnd();
				}
				if (struct.userName != null) {
					oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
					oprot.writeString(struct.userName);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldBegin(AREA_ID_FIELD_DESC);
				oprot.writeI32(struct.areaId);
				oprot.writeFieldEnd();
				if (struct.type != null) {
					oprot.writeFieldBegin(TYPE_FIELD_DESC);
					oprot.writeString(struct.type);
					oprot.writeFieldEnd();
				}
				if (struct.fashion != null) {
					oprot.writeFieldBegin(FASHION_FIELD_DESC);
					oprot.writeString(struct.fashion);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryConditionDayKPI_argsTupleSchemeFactory implements SchemeFactory {
			public queryConditionDayKPI_argsTupleScheme getScheme() {
				return new queryConditionDayKPI_argsTupleScheme();
			}
		}

		private static class queryConditionDayKPI_argsTupleScheme extends TupleScheme<queryConditionDayKPI_args> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryConditionDayKPI_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetBeginDate()) {
					optionals.set(0);
				}
				if (struct.isSetEndDate()) {
					optionals.set(1);
				}
				if (struct.isSetKpiCode()) {
					optionals.set(2);
				}
				if (struct.isSetUserName()) {
					optionals.set(3);
				}
				if (struct.isSetAreaId()) {
					optionals.set(4);
				}
				if (struct.isSetType()) {
					optionals.set(5);
				}
				if (struct.isSetFashion()) {
					optionals.set(6);
				}
				oprot.writeBitSet(optionals, 7);
				if (struct.isSetBeginDate()) {
					oprot.writeString(struct.beginDate);
				}
				if (struct.isSetEndDate()) {
					oprot.writeString(struct.endDate);
				}
				if (struct.isSetKpiCode()) {
					oprot.writeString(struct.kpiCode);
				}
				if (struct.isSetUserName()) {
					oprot.writeString(struct.userName);
				}
				if (struct.isSetAreaId()) {
					oprot.writeI32(struct.areaId);
				}
				if (struct.isSetType()) {
					oprot.writeString(struct.type);
				}
				if (struct.isSetFashion()) {
					oprot.writeString(struct.fashion);
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryConditionDayKPI_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(7);
				if (incoming.get(0)) {
					struct.beginDate = iprot.readString();
					struct.setBeginDateIsSet(true);
				}
				if (incoming.get(1)) {
					struct.endDate = iprot.readString();
					struct.setEndDateIsSet(true);
				}
				if (incoming.get(2)) {
					struct.kpiCode = iprot.readString();
					struct.setKpiCodeIsSet(true);
				}
				if (incoming.get(3)) {
					struct.userName = iprot.readString();
					struct.setUserNameIsSet(true);
				}
				if (incoming.get(4)) {
					struct.areaId = iprot.readI32();
					struct.setAreaIdIsSet(true);
				}
				if (incoming.get(5)) {
					struct.type = iprot.readString();
					struct.setTypeIsSet(true);
				}
				if (incoming.get(6)) {
					struct.fashion = iprot.readString();
					struct.setFashionIsSet(true);
				}
			}
		}

	}

	public static class queryConditionDayKPI_result implements
			org.apache.thrift.TBase<queryConditionDayKPI_result, queryConditionDayKPI_result._Fields>,
			java.io.Serializable, Cloneable, Comparable<queryConditionDayKPI_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryConditionDayKPI_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"success", org.apache.thrift.protocol.TType.MAP, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryConditionDayKPI_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryConditionDayKPI_resultTupleSchemeFactory());
		}

		public Map<String, String> success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 0: // SUCCESS
						return SUCCESS;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.SUCCESS,
					new org.apache.thrift.meta_data.FieldMetaData("success",
							org.apache.thrift.TFieldRequirementType.DEFAULT,
							new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP,
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING),
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING))));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(queryConditionDayKPI_result.class,
					metaDataMap);
		}

		public queryConditionDayKPI_result() {
		}

		public queryConditionDayKPI_result(Map<String, String> success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryConditionDayKPI_result(queryConditionDayKPI_result other) {
			if (other.isSetSuccess()) {
				Map<String, String> __this__success = new HashMap<String, String>(other.success);
				this.success = __this__success;
			}
		}

		public queryConditionDayKPI_result deepCopy() {
			return new queryConditionDayKPI_result(this);
		}

		public void clear() {
			this.success = null;
		}

		public int getSuccessSize() {
			return (this.success == null) ? 0 : this.success.size();
		}

		public void putToSuccess(String key, String val) {
			if (this.success == null) {
				this.success = new HashMap<String, String>();
			}
			this.success.put(key, val);
		}

		public Map<String, String> getSuccess() {
			return this.success;
		}

		public queryConditionDayKPI_result setSuccess(Map<String, String> success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case SUCCESS:
					if (value == null) {
						unsetSuccess();
					} else {
						setSuccess((Map<String, String>) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case SUCCESS:
					return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case SUCCESS:
					return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryConditionDayKPI_result)
				return this.equals((queryConditionDayKPI_result) that);
			return false;
		}

		public boolean equals(queryConditionDayKPI_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryConditionDayKPI_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryConditionDayKPI_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryConditionDayKPI_resultStandardSchemeFactory implements SchemeFactory {
			public queryConditionDayKPI_resultStandardScheme getScheme() {
				return new queryConditionDayKPI_resultStandardScheme();
			}
		}

		private static class queryConditionDayKPI_resultStandardScheme extends
				StandardScheme<queryConditionDayKPI_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryConditionDayKPI_result struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 0: // SUCCESS
							if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
								{
									org.apache.thrift.protocol.TMap _map10 = iprot.readMapBegin();
									struct.success = new HashMap<String, String>(2 * _map10.size);
									for (int _i11 = 0; _i11 < _map10.size; ++_i11) {
										String _key12;
										String _val13;
										_key12 = iprot.readString();
										_val13 = iprot.readString();
										struct.success.put(_key12, _val13);
									}
									iprot.readMapEnd();
								}
								struct.setSuccessIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryConditionDayKPI_result struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					{
						oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								struct.success.size()));
						for (Map.Entry<String, String> _iter14 : struct.success.entrySet()) {
							oprot.writeString(_iter14.getKey());
							oprot.writeString(_iter14.getValue());
						}
						oprot.writeMapEnd();
					}
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryConditionDayKPI_resultTupleSchemeFactory implements SchemeFactory {
			public queryConditionDayKPI_resultTupleScheme getScheme() {
				return new queryConditionDayKPI_resultTupleScheme();
			}
		}

		private static class queryConditionDayKPI_resultTupleScheme extends TupleScheme<queryConditionDayKPI_result> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryConditionDayKPI_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					{
						oprot.writeI32(struct.success.size());
						for (Map.Entry<String, String> _iter15 : struct.success.entrySet()) {
							oprot.writeString(_iter15.getKey());
							oprot.writeString(_iter15.getValue());
						}
					}
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryConditionDayKPI_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					{
						org.apache.thrift.protocol.TMap _map16 = new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								iprot.readI32());
						struct.success = new HashMap<String, String>(2 * _map16.size);
						for (int _i17 = 0; _i17 < _map16.size; ++_i17) {
							String _key18;
							String _val19;
							_key18 = iprot.readString();
							_val19 = iprot.readString();
							struct.success.put(_key18, _val19);
						}
					}
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class queryDetail_args implements
			org.apache.thrift.TBase<queryDetail_args, queryDetail_args._Fields>, java.io.Serializable, Cloneable,
			Comparable<queryDetail_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryDetail_args");

		private static final org.apache.thrift.protocol.TField BEGIN_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"beginDate", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField END_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"endDate", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"userName", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryDetail_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryDetail_argsTupleSchemeFactory());
		}

		public String beginDate; // required
		public String endDate; // required
		public String userName; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			BEGIN_DATE((short) 1, "beginDate"), END_DATE((short) 2, "endDate"), USER_NAME((short) 3, "userName");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 1: // BEGIN_DATE
						return BEGIN_DATE;
					case 2: // END_DATE
						return END_DATE;
					case 3: // USER_NAME
						return USER_NAME;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.BEGIN_DATE, new org.apache.thrift.meta_data.FieldMetaData("beginDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.END_DATE, new org.apache.thrift.meta_data.FieldMetaData("endDate",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("userName",
					org.apache.thrift.TFieldRequirementType.DEFAULT,
					new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(queryDetail_args.class, metaDataMap);
		}

		public queryDetail_args() {
		}

		public queryDetail_args(String beginDate, String endDate, String userName) {
			this();
			this.beginDate = beginDate;
			this.endDate = endDate;
			this.userName = userName;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryDetail_args(queryDetail_args other) {
			if (other.isSetBeginDate()) {
				this.beginDate = other.beginDate;
			}
			if (other.isSetEndDate()) {
				this.endDate = other.endDate;
			}
			if (other.isSetUserName()) {
				this.userName = other.userName;
			}
		}

		public queryDetail_args deepCopy() {
			return new queryDetail_args(this);
		}

		public void clear() {
			this.beginDate = null;
			this.endDate = null;
			this.userName = null;
		}

		public String getBeginDate() {
			return this.beginDate;
		}

		public queryDetail_args setBeginDate(String beginDate) {
			this.beginDate = beginDate;
			return this;
		}

		public void unsetBeginDate() {
			this.beginDate = null;
		}

		/**
		 * Returns true if field beginDate is set (has been assigned a value)
		 * and false otherwise
		 */
		public boolean isSetBeginDate() {
			return this.beginDate != null;
		}

		public void setBeginDateIsSet(boolean value) {
			if (!value) {
				this.beginDate = null;
			}
		}

		public String getEndDate() {
			return this.endDate;
		}

		public queryDetail_args setEndDate(String endDate) {
			this.endDate = endDate;
			return this;
		}

		public void unsetEndDate() {
			this.endDate = null;
		}

		/**
		 * Returns true if field endDate is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetEndDate() {
			return this.endDate != null;
		}

		public void setEndDateIsSet(boolean value) {
			if (!value) {
				this.endDate = null;
			}
		}

		public String getUserName() {
			return this.userName;
		}

		public queryDetail_args setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public void unsetUserName() {
			this.userName = null;
		}

		/**
		 * Returns true if field userName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetUserName() {
			return this.userName != null;
		}

		public void setUserNameIsSet(boolean value) {
			if (!value) {
				this.userName = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case BEGIN_DATE:
					if (value == null) {
						unsetBeginDate();
					} else {
						setBeginDate((String) value);
					}
					break;

				case END_DATE:
					if (value == null) {
						unsetEndDate();
					} else {
						setEndDate((String) value);
					}
					break;

				case USER_NAME:
					if (value == null) {
						unsetUserName();
					} else {
						setUserName((String) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case BEGIN_DATE:
					return getBeginDate();

				case END_DATE:
					return getEndDate();

				case USER_NAME:
					return getUserName();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case BEGIN_DATE:
					return isSetBeginDate();
				case END_DATE:
					return isSetEndDate();
				case USER_NAME:
					return isSetUserName();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryDetail_args)
				return this.equals((queryDetail_args) that);
			return false;
		}

		public boolean equals(queryDetail_args that) {
			if (that == null)
				return false;

			boolean this_present_beginDate = true && this.isSetBeginDate();
			boolean that_present_beginDate = true && that.isSetBeginDate();
			if (this_present_beginDate || that_present_beginDate) {
				if (!(this_present_beginDate && that_present_beginDate))
					return false;
				if (!this.beginDate.equals(that.beginDate))
					return false;
			}

			boolean this_present_endDate = true && this.isSetEndDate();
			boolean that_present_endDate = true && that.isSetEndDate();
			if (this_present_endDate || that_present_endDate) {
				if (!(this_present_endDate && that_present_endDate))
					return false;
				if (!this.endDate.equals(that.endDate))
					return false;
			}

			boolean this_present_userName = true && this.isSetUserName();
			boolean that_present_userName = true && that.isSetUserName();
			if (this_present_userName || that_present_userName) {
				if (!(this_present_userName && that_present_userName))
					return false;
				if (!this.userName.equals(that.userName))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryDetail_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetBeginDate()).compareTo(other.isSetBeginDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetBeginDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.beginDate, other.beginDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetEndDate()).compareTo(other.isSetEndDate());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetEndDate()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.endDate, other.endDate);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetUserName()).compareTo(other.isSetUserName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetUserName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userName, other.userName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryDetail_args(");
			boolean first = true;

			sb.append("beginDate:");
			if (this.beginDate == null) {
				sb.append("null");
			} else {
				sb.append(this.beginDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("endDate:");
			if (this.endDate == null) {
				sb.append("null");
			} else {
				sb.append(this.endDate);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("userName:");
			if (this.userName == null) {
				sb.append("null");
			} else {
				sb.append(this.userName);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryDetail_argsStandardSchemeFactory implements SchemeFactory {
			public queryDetail_argsStandardScheme getScheme() {
				return new queryDetail_argsStandardScheme();
			}
		}

		private static class queryDetail_argsStandardScheme extends StandardScheme<queryDetail_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryDetail_args struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 1: // BEGIN_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.beginDate = iprot.readString();
								struct.setBeginDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 2: // END_DATE
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.endDate = iprot.readString();
								struct.setEndDateIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						case 3: // USER_NAME
							if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
								struct.userName = iprot.readString();
								struct.setUserNameIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryDetail_args struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.beginDate != null) {
					oprot.writeFieldBegin(BEGIN_DATE_FIELD_DESC);
					oprot.writeString(struct.beginDate);
					oprot.writeFieldEnd();
				}
				if (struct.endDate != null) {
					oprot.writeFieldBegin(END_DATE_FIELD_DESC);
					oprot.writeString(struct.endDate);
					oprot.writeFieldEnd();
				}
				if (struct.userName != null) {
					oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
					oprot.writeString(struct.userName);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryDetail_argsTupleSchemeFactory implements SchemeFactory {
			public queryDetail_argsTupleScheme getScheme() {
				return new queryDetail_argsTupleScheme();
			}
		}

		private static class queryDetail_argsTupleScheme extends TupleScheme<queryDetail_args> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryDetail_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetBeginDate()) {
					optionals.set(0);
				}
				if (struct.isSetEndDate()) {
					optionals.set(1);
				}
				if (struct.isSetUserName()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetBeginDate()) {
					oprot.writeString(struct.beginDate);
				}
				if (struct.isSetEndDate()) {
					oprot.writeString(struct.endDate);
				}
				if (struct.isSetUserName()) {
					oprot.writeString(struct.userName);
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryDetail_args struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.beginDate = iprot.readString();
					struct.setBeginDateIsSet(true);
				}
				if (incoming.get(1)) {
					struct.endDate = iprot.readString();
					struct.setEndDateIsSet(true);
				}
				if (incoming.get(2)) {
					struct.userName = iprot.readString();
					struct.setUserNameIsSet(true);
				}
			}
		}

	}

	public static class queryDetail_result implements
			org.apache.thrift.TBase<queryDetail_result, queryDetail_result._Fields>, java.io.Serializable, Cloneable,
			Comparable<queryDetail_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"queryDetail_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"success", org.apache.thrift.protocol.TType.MAP, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();

		static {
			schemes.put(StandardScheme.class, new queryDetail_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new queryDetail_resultTupleSchemeFactory());
		}

		public Map<String, String> success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
					case 0: // SUCCESS
						return SUCCESS;
					default:
						return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.SUCCESS,
					new org.apache.thrift.meta_data.FieldMetaData("success",
							org.apache.thrift.TFieldRequirementType.DEFAULT,
							new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP,
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING),
									new org.apache.thrift.meta_data.FieldValueMetaData(
											org.apache.thrift.protocol.TType.STRING))));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(queryDetail_result.class, metaDataMap);
		}

		public queryDetail_result() {
		}

		public queryDetail_result(Map<String, String> success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public queryDetail_result(queryDetail_result other) {
			if (other.isSetSuccess()) {
				Map<String, String> __this__success = new HashMap<String, String>(other.success);
				this.success = __this__success;
			}
		}

		public queryDetail_result deepCopy() {
			return new queryDetail_result(this);
		}

		public void clear() {
			this.success = null;
		}

		public int getSuccessSize() {
			return (this.success == null) ? 0 : this.success.size();
		}

		public void putToSuccess(String key, String val) {
			if (this.success == null) {
				this.success = new HashMap<String, String>();
			}
			this.success.put(key, val);
		}

		public Map<String, String> getSuccess() {
			return this.success;
		}

		public queryDetail_result setSuccess(Map<String, String> success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
				case SUCCESS:
					if (value == null) {
						unsetSuccess();
					} else {
						setSuccess((Map<String, String>) value);
					}
					break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
				case SUCCESS:
					return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
				case SUCCESS:
					return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof queryDetail_result)
				return this.equals((queryDetail_result) that);
			return false;
		}

		public boolean equals(queryDetail_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		public int hashCode() {
			return 0;
		}

		public int compareTo(queryDetail_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("queryDetail_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class queryDetail_resultStandardSchemeFactory implements SchemeFactory {
			public queryDetail_resultStandardScheme getScheme() {
				return new queryDetail_resultStandardScheme();
			}
		}

		private static class queryDetail_resultStandardScheme extends StandardScheme<queryDetail_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, queryDetail_result struct)
					throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
						case 0: // SUCCESS
							if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
								{
									org.apache.thrift.protocol.TMap _map20 = iprot.readMapBegin();
									struct.success = new HashMap<String, String>(2 * _map20.size);
									for (int _i21 = 0; _i21 < _map20.size; ++_i21) {
										String _key22;
										String _val23;
										_key22 = iprot.readString();
										_val23 = iprot.readString();
										struct.success.put(_key22, _val23);
									}
									iprot.readMapEnd();
								}
								struct.setSuccessIsSet(true);
							} else {
								org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
							}
							break;
						default:
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, queryDetail_result struct)
					throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					{
						oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								struct.success.size()));
						for (Map.Entry<String, String> _iter24 : struct.success.entrySet()) {
							oprot.writeString(_iter24.getKey());
							oprot.writeString(_iter24.getValue());
						}
						oprot.writeMapEnd();
					}
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class queryDetail_resultTupleSchemeFactory implements SchemeFactory {
			public queryDetail_resultTupleScheme getScheme() {
				return new queryDetail_resultTupleScheme();
			}
		}

		private static class queryDetail_resultTupleScheme extends TupleScheme<queryDetail_result> {

			public void write(org.apache.thrift.protocol.TProtocol prot, queryDetail_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					{
						oprot.writeI32(struct.success.size());
						for (Map.Entry<String, String> _iter25 : struct.success.entrySet()) {
							oprot.writeString(_iter25.getKey());
							oprot.writeString(_iter25.getValue());
						}
					}
				}
			}

			public void read(org.apache.thrift.protocol.TProtocol prot, queryDetail_result struct)
					throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					{
						org.apache.thrift.protocol.TMap _map26 = new org.apache.thrift.protocol.TMap(
								org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING,
								iprot.readI32());
						struct.success = new HashMap<String, String>(2 * _map26.size);
						for (int _i27 = 0; _i27 < _map26.size; ++_i27) {
							String _key28;
							String _val29;
							_key28 = iprot.readString();
							_val29 = iprot.readString();
							struct.success.put(_key28, _val29);
						}
					}
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

}
