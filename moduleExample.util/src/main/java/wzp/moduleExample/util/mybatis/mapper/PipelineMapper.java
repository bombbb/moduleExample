package wzp.moduleExample.util.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import wzp.moduleExample.util.mybatis.bo.SvcLinkRestResponse;
import wzp.moduleExample.util.mybatis.bo.SvcLinkSql;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;

public interface PipelineMapper {

	@Update("UPDATE sequence SET current_value=current_value+increment " + "WHERE name=#{seqName,jdbcType=VARCHAR}")
	int setNextSeq(@Param("seqName") String seqName);

	@Select("SELECT current_value FROM sequence WHERE name=#{seqName,jdbcType=VARCHAR}")
	long getCurrentSeq(@Param("seqName") String seqName);

	@Insert({ "<script>",
			"INSERT INTO svc_link_sql(`sql_id`, `link_id`, `app`, `timeout`, `sql_text`, `exception`, `exception_message`, `span`, `ip`, `timestamp`) ",
			"VALUES<foreach collection='records' item='record' open='(' separator = '),(' close=')' >#{record.sqlId,jdbcType=VARCHAR},#{record.linkId,jdbcType=VARCHAR},#{record.app,jdbcType=VARCHAR},#{record.timeout,jdbcType=BIT},#{record.sqlText,jdbcType=VARCHAR},#{record.exception,jdbcType=BIT},#{record.exceptionMessage,jdbcType=VARCHAR},#{record.span,jdbcType=BIGINT},#{record.ip,jdbcType=VARCHAR},#{record.timestamp,jdbcType=TIMESTAMP}</foreach> ",
			"ON DUPLICATE KEY UPDATE app=app", "</script>" })
	int insertSvcLinkSql(@Param("records") List<SvcLinkSql> records);

	@Insert({ "<script>",
			"INSERT INTO svc_link_rest_response(`svc_id`, `link_id`, `app`, `timeout`, `url`, `exception`, `exception_message`, `span`, `ip`, `client_ip`, `timestamp`) ",
			"VALUES<foreach collection='records' item='record' open='(' separator = '),(' close=')' >#{record.svcId,jdbcType=VARCHAR},#{record.linkId,jdbcType=VARCHAR},#{record.app,jdbcType=VARCHAR},#{record.timeout,jdbcType=BIT},#{record.url,jdbcType=VARCHAR},#{record.exception,jdbcType=BIT},#{record.exceptionMessage,jdbcType=VARCHAR},#{record.span,jdbcType=BIGINT},#{record.ip,jdbcType=VARCHAR},#{record.clientIp,jdbcType=VARCHAR},#{record.timestamp,jdbcType=TIMESTAMP}</foreach> ",
			"ON DUPLICATE KEY UPDATE app=app", "</script>" })
	int insertSvcLinkRestResponse(@Param("records") List<SvcLinkRestResponse> records);
}
