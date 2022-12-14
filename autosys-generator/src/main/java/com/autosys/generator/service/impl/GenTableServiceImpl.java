package com.autosys.generator.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.autosys.common.core.constants.CommonConstant;
import com.autosys.common.core.constants.enums.ResultCodeEnum;
import com.autosys.common.core.exception.ApiException;
import com.autosys.common.core.util.StringUtils;
import com.autosys.common.core.util.text.CharsetKit;
import com.autosys.generator.constant.GeneratorConstants;
import com.autosys.generator.entity.GenTable;
import com.autosys.generator.entity.GenTableColumn;
import com.autosys.generator.mapper.IGenTableColumnMapper;
import com.autosys.generator.mapper.IGenTableMapper;
import com.autosys.generator.model.GenTableParamModel;
import com.autosys.generator.service.IGenTableService;
import com.autosys.generator.util.GeneratorUtils;
import com.autosys.generator.util.VelocityInitializer;
import com.autosys.generator.util.VelocityUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GenTableServiceImpl extends ServiceImpl<IGenTableMapper, GenTable> implements IGenTableService
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Resource
    private IGenTableMapper genTableMapper;

    @Resource
    private IGenTableColumnMapper genTableColumnMapper;

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return ????????????
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    @Override
    public IPage<GenTable> selectGenTableList(Page<GenTable> page, GenTableParamModel paramModel){
        return genTableMapper.selectGenTableList(page, paramModel);
    }

    /**
     * ??????????????????
     *
     * @param genTable ????????????
     * @return ??????????????????
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * ??????????????????
     *
     * @param tableNames ????????????
     * @return ??????????????????
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    @Override
    public List<GenTable> selectGenTableAll()
    {
        return genTableMapper.selectGenTableAll();
    }

    /**
     * ????????????
     *
     * @param genTable ????????????
     * @return ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0)
        {
            for (GenTableColumn cenTableColumn : genTable.getColumns())
            {
                genTableColumnMapper.updateGenTableColumn(cenTableColumn);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param tableIds ?????????????????????ID
     * @return ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGenTableByIds(Long[] tableIds)
    {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * ???????????????
     *
     * @param tableList ???????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importGenTable(List<GenTable> tableList)
    {
            //???????????????????????????
//        String operName = SecurityUtils.getUsername();
        String operName = null;
        try
        {
            for (GenTable table : tableList)
            {
                String tableName = table.getTableName();
                GeneratorUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row > 0)
                {
                    // ???????????????
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns)
                    {
                        GeneratorUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"???????????????" + e.getMessage());
        }
    }

    /**
     * ????????????
     *
     * @param tableId ?????????
     * @return ??????????????????
     */
    @Override
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // ???????????????
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // ?????????????????????
        setSubTable(table);
        // ?????????????????????
        setPkColumn(table);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // ??????????????????
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // ????????????
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * ??????????????????????????????
     *
     * @param tableName ?????????
     * @return ??????
     */
    @Override
    public byte[] downloadCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * ?????????????????????????????????
     *
     * @param tableName ?????????
     */
    @Override
    public void generatorCode(String tableName)
    {
        // ???????????????
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // ?????????????????????
        setSubTable(table);
        // ?????????????????????
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // ??????????????????
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm"))
            {
                // ????????????
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
                tpl.merge(context, sw);
                try
                {
                    String path = getGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                }
                catch (IOException e)
                {
                    throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"??????????????????????????????" + table.getTableName());
                }
            }
        }
    }

    /**
     * ???????????????
     *
     * @param tableName ?????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchDb(String tableName)
    {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        Map<String, GenTableColumn> tableColumnMap = tableColumns.stream().collect(Collectors.toMap(GenTableColumn::getColumnName, Function.identity()));

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns))
        {
            throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"??????????????????????????????????????????");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            GeneratorUtils.initColumnField(column, table);
            if (tableColumnMap.containsKey(column.getColumnName()))
            {
                GenTableColumn prevColumn = tableColumnMap.get(column.getColumnName());
                column.setColumnId(prevColumn.getColumnId());
                if (column.isList())
                {
                    // ??????????????????????????????????????????/??????????????????
                    column.setDictType(prevColumn.getDictType());
                    column.setQueryType(prevColumn.getQueryType());
                }
                if (StringUtils.isNotEmpty(prevColumn.getIsRequired()) && !column.isPk()
                        && (column.isInsert() || column.isEdit())
                        && ((column.isUsableColumn()) || (!column.isSuperColumn())))
                {
                    // ?????????(??????/??????&?????????/?????????????????????)?????????????????????/??????????????????
                    column.setIsRequired(prevColumn.getIsRequired());
                    column.setHtmlType(prevColumn.getHtmlType());
                }
                genTableColumnMapper.updateGenTableColumn(column);
            }
            else
            {
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns))
        {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param tableNames ?????????
     * @return ??????
     */
    @Override
    public byte[] downloadCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * ??????????????????????????????
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // ???????????????
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // ?????????????????????
        setSubTable(table);
        // ?????????????????????
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // ??????????????????
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // ????????????
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
            tpl.merge(context, sw);
            try
            {
                // ?????????zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, CommonConstant.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("??????????????????????????????" + table.getTableName(), e);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param genTable ????????????
     */
    @Override
    public void validateEdit(GenTable genTable)
    {
        if (GeneratorConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSON.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GeneratorConstants.TREE_CODE)))
            {
                throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"???????????????????????????");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GeneratorConstants.TREE_PARENT_CODE)))
            {
                throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"??????????????????????????????");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GeneratorConstants.TREE_NAME)))
            {
                throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"???????????????????????????");
            }
            else if (GeneratorConstants.TPL_SUB.equals(genTable.getTplCategory()))
            {
                if (StringUtils.isEmpty(genTable.getSubTableName()))
                {
                    throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"?????????????????????????????????");
                }
                else if (StringUtils.isEmpty(genTable.getSubTableFkName()))
                {
                    throw new ApiException(ResultCodeEnum.FAILED.getResultCode(),"????????????????????????????????????");
                }
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param table ???????????????
     */
    public void setPkColumn(GenTable table)
    {
        for (GenTableColumn column : table.getColumns())
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GeneratorConstants.TPL_SUB.equals(table.getTplCategory()))
        {
            for (GenTableColumn column : table.getSubTable().getColumns())
            {
                if (column.isPk())
                {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn()))
            {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param table ???????????????
     */
    public void setSubTable(GenTable table)
    {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName))
        {
            table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param genTable ????????????????????????
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSON.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GeneratorConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GeneratorConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GeneratorConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GeneratorConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GeneratorConstants.PARENT_MENU_NAME);

            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * ????????????????????????
     *
     * @param table ???????????????
     * @param template ??????????????????
     * @return ????????????
     */
    public static String getGenPath(GenTable table, String template)
    {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, "/"))
        {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }
}
