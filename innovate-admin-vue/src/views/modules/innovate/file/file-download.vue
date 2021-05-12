<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          @change="getDataList"
          v-model="dataForm.matchTime"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.projectName" placeholder="项目名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item :label="'文件总数：' + attachTotal + '个  下载第x~y个文件'">
        <el-input-number v-model="pageI" :step="1" step-strictly></el-input-number>
      </el-form-item>
      <el-form-item label="">
        <el-input-number v-model="pageS" :step="10" step-strictly></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button @click="attachDownAll()">下载全部文件</el-button>
      </el-form-item>
    </el-form>
    <el-card>
      <el-radio-group v-model="hasApply" >
<!--        @change="getDataList"-->
        <el-radio label="1">双创赛事</el-radio>
<!--        <el-radio label="2">大创立项</el-radio>-->
<!--        <el-radio label="3">大创结题</el-radio>-->
      </el-radio-group>
    </el-card>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'matchInfoEntity.matchId', order: 'ascending'}"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        hidden
        header-align="center"
        align="center"
        width="50"
        label="ID">
        <template slot-scope="props">
          <p v-text="props.$index+1"></p>
        </template>
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity.matchName"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
<!--      <el-table-column-->
<!--        sortable-->
<!--        prop="matchInfoEntity.matchGroupType"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="参赛组别">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 1" size="small">创意组</el-tag>-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 2" size="small">初创组</el-tag>-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 3" size="small">成长组</el-tag>-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 4" size="small">师生共创组</el-tag>-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 5" size="small">"青年红色梦之旅"赛道</el-tag>-->
<!--          <el-tag v-if="scope.row.matchInfoEntity.matchGroupType === 6" size="small">其它</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        prop="matchInfoEntity.matchDescribe"
        header-align="center"
        align="center"
        label="项目概述">
      </el-table-column>
      <el-table-column
        header-align="center"
        align="left"
        label="文件">
        <template slot-scope="props">
          <el-row>
            <el-col :span="21" v-for="item in props.row.matchAttachEntities">
              <el-tag>{{ item.attachName }}</el-tag>
              <el-button @click="attachDown(item.attachPath)">下载</el-button>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 下载 -->

  </div>
</template>

<script>


export default {
  data () {
    return {
      canUpdateVisibles: false,
      retreatVisible: false,
      projectList: [],
      eventLists: this.$store.state.eventLists,
      sysTeacherEntities: [],
      hasApply: '1',
      dataForm: {
        baseId: '',
        projectName: '',
        matchTime: new Date()
      },
      statusList: [
        {value: 1, label: '农、林、牧、渔业'}, {value: 2, label: '采矿业'},
        {value: 3, label: '制造业'}, {value: 4, label: '电力、热力、燃气及水的生产和供应业'},
        {value: 5, label: '环境和公共设施管理业'}, {value: 6, label: '建筑业'},
        {value: 7, label: '交通运输、仓储业和邮政业'}, {value: 8, label: '信息传输、计算机服务和软件业'},
        {value: 9, label: '批发和零售业'}, {value: 10, label: '住宿、餐饮业'},
        {value: 11, label: '金融、保险业'}, {value: 12, label: '房地产业'},
        {value: 13, label: '租赁和商务服务业'}, {value: 14, label: '科学研究、技术服务和地质勘查业'},
        {value: 15, label: '水利、环境和公共设施管理业'}, {value: 16, label: '居民服务和其他服务业'},
        {value: 17, label: '教育'}, {value: 18, label: '卫生、社会保障和社会服务业'},
        {value: 19, label: '文化、体育、娱乐业'}, {value: 20, label: '综合（含投资类、主业不明显）'},
        {value: 21, label: '其它'}
      ],
      matchGroupTypeList: [
        {value: 1, label: '创意组'}, {value: 2, label: '初创组'},
        {value: 3, label: '成长组'}, {value: 4, label: '师生共创组'},
        {value: 5, label: '"青年红色梦之旅"赛道'}, {value: 6, label: '其它'}
      ],
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      detailVisible: false,
      applyVisible: false,
      reviewAddOrUpdateVisible: false,
      reApplyButtonVisible: 'false',
      totalFile: 0,//下载文件个数
      percent: 0, // 已下载个数
      pageI: 1,
      pageS: 10,
      attachTotal: 0// 文件总数
    }
  },
  components: {
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.addOrUpdateVisible = false
      this.detailVisible = false
      this.reviewAddOrUpdateVisible = false
      this.reApplyButtonVisible = 'false'


      this.$nextTick(() => {
        this.$http({
          url: this.$http.adornUrl('/innovate/match/info/list'),
          method: 'get',
          params: this.$http.adornParams({
            'projectName': this.dataForm.projectName,
            'matchTime': this.dataForm.matchTime == null ? '' : this.dataForm.matchTime.getFullYear(),
            'currPage': this.pageIndex,
            'pageSize': this.pageSize,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
            this.attachTotal  = data.attachTotal
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.reApplyButtonVisible = 'false'
          this.dataListLoading = false
        })
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },

    retreatIsVisible (item) {
      if (this.isAuth('innovate:match:retreat')) {
        if (item.projectMatchApplyStatus !== null || item.projectMatchApplyStatus !== '') {
          if (item.projectMatchApplyStatus === 3) {
            let roleIdList = this.$store.state.user.roleIdList
            for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
              if (roleIdList[roleIndex] === 5) {
                return true
              }
            }
          }
        }
      }
      return false
    },
    applyIsVisible (item) {
      if (this.isAuth('innovate:project:apply:match')) {
        if (item.projectMatchApplyStatus !== null || item.projectMatchApplyStatus !== '') {
          if (item.projectMatchApplyStatus === 3) {
            let roleIdList = this.$store.state.user.roleIdList
            for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
              if (roleIdList[roleIndex] === 5) {
                return true
              }
            }
          }
        }
      }
      return false
    },

    reApplyIsVisible (item) {
      if (this.isAuth('innovate:project:apply:match')) {
        if (item.projectMatchApplyStatus !== null || item.projectMatchApplyStatus !== '') {
          if (item.projectMatchApplyStatus === 4) {
            let roleIdList = this.$store.state.user.roleIdList
            for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
              if (roleIdList[roleIndex] === 5) {
                this.reApplyButtonVisible = 'true'
                return true
              }
            }
          }
        }
      }
      return false
    },
    // 下载
    attachDown (attachPath) {
      this.$http({
        url: this.$http.adornUrl(`/innovate/match/attach/downloadurl`),
        method: 'post',
        params: this.$http.adornParams({
          'filePath': attachPath
        })
      }).then(({data}) => {
        window.open(data.url)
      }).catch(err => {
        console.log(err)
      })

    },
    // 下载全部文件
    attachDownAll () {
      // let timer = new Function();
      this.$notify({
        title: '文件正在下载中...',
        message: `下载进度：${this.totalFile}/${this.percent}`,
        duration: 0,
        type: 'info'
      })
      // timer = setInterval(()=> {
      //   this.$http({
      //     url: this.$http.adornUrl(`/sys/oss/flushProgress`),
      //     method: 'post',
      //     params: this.$http.adornParams({})
      //   }).then(res => {
      //     this.totalFile = res.totalFile
      //     this.percent = res.percent
      //   })
      //
      // }, 1500)

      this.$httpFile({
        url: this.$http.adornUrl(`/sys/oss/downloadFile`),
        method: 'post',
        params: this.$httpFile.adornParams({
          matchTime: this.dataForm.matchTime.getFullYear(),
          pageIndex: this.pageI,
          pageSize: this.pageS
        })
      }).then(response => {

        if (!response) {
          this.$notify({
            title: '下载提示',
            message: '下载失败',
            duration: 0,
            type: 'error'
          })
          this.downloadLoading = false
          return
        }
        let url = window.URL.createObjectURL(new Blob([response.data]))
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', '项目文件.zip')
        document.body.appendChild(link)
        link.click()
        this.$notify({
          title: '下载成功',
          message: '后台文件下载成功',
          duration: 0,
          type: 'success'
        })
      }).catch(err => {
        clearInterval(timer);
        console.log(err)
      })

    },
    // 详情
    detailHandle (id) {
      this.detailVisible = true
      this.$nextTick(() => {
        this.$refs.detail.init(id)
      })
    }
  }
}
</script>
<style>
.el-step__title {
  font-size: 12px;
  line-height: 28px;
}
.el-table__expanded-cell[class*=cell] {
  padding: 5px 5px;
}
</style>
