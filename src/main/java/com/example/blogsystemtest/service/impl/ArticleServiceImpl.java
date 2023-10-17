package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddArticleDto;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.LikeDto;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.domain.entity.ArticleTag;
import com.example.blogsystemtest.domain.entity.Category;
import com.example.blogsystemtest.domain.vo.*;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.ArticleMapper;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.service.ArticleTagService;
import com.example.blogsystemtest.service.CategoryService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.RedisCache;
import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult hotArticleList() {

        LinkedHashSet<HotArticleVo> set=redisCache.getCacheZSetValue("article:hot",0,99);
        //分页，一页10条
        List<HotArticleVo> list=getPaginatedData(set,1,10);

        //bean拷贝
        /*List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            articleVos.add(vo);
        }*/

        return ResponseResult.okResult(list);
    }

    public <T> List<T> getPaginatedData(LinkedHashSet<T> dataList, int pageNumber, int pageSize) {
        List<T> list=new ArrayList<>(dataList);
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, dataList.size());

        if (startIndex >= endIndex) {
            return Collections.emptyList();
        }

        return list.subList(startIndex, endIndex);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId就要 查询时要传入相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);

        List<Article> articles = page(page,lambdaQueryWrapper).getRecords();
        //articleId去查询articleName进行设置
        /*for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/
         articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        //查询categoryName

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //TODO 这里需要改成从redis中读取文章详情

    @Override
    public ResponseResult getArticleDetail(Long id) {
        ArticleDetailVo articleDetailVo=redisCache.getCacheMapValue("article:detail",id.toString());
        if(Objects.isNull(articleDetailVo)){
            //根据id查询文章
            Article article = getById(id);
            //从redis中获取viewCount
            Long viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
            if(Objects.isNull(viewCount)){
                viewCount=articleMapper.selectById(id).getViewCount();
                redisCache.setCacheMapValue("article:viewCount",id.toString(),viewCount);
            }
            updateViewCount(id);
            article.setViewCount(viewCount+1L);

            //封装查询结果
            articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
            Long categoryId = articleDetailVo.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                articleDetailVo.setCategoryName(category.getName());
            }
            redisCache.setCacheMapValue("article:detail",id.toString(),articleDetailVo);
        }
        return ResponseResult.okResult(articleDetailVo);
    }


    public void updateViewCount(Long id) {
        //更新redis中对应ID的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //分页查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(articleListDto)){
            queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
            queryWrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
            queryWrapper.eq(Objects.nonNull(articleListDto.getUserId()),Article::getCreateBy,articleListDto.getUserId());
        }

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Article> articleList = page(page, queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<ArticleVo> ArticleVo = BeanCopyUtils.copyBeanList(articleList.getRecords(), ArticleVo.class);
        PageVo  pageVo = new PageVo(ArticleVo,articleList.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getBaseMapper().selectById(id);
//        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ArticleTag::getArticleId,id);
//        queryWrapper.select(ArticleTag::getTagId);
//        List<ArticleTag> list = articleTagService.list(queryWrapper);
//        List<Long> longList = new ArrayList<>();
//        for (ArticleTag articleTag : list) {
//            Long tagId = articleTag.getTagId();
//            longList.add(tagId);
//        }
        List<Long> tagList = articleTagService.getTagList(article.getId());
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTags(tagList);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult update(ArticleVo articleVo) {
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        updateById(article);
        //更新前的tag集合
        List<Long> tagList = articleTagService.getTagList(articleVo.getId());
        //getArticleVo.getTags()更新后的tag集合
        List<ArticleTag> articleTags = articleVo.getTags().stream()
                .filter(tagId->!tagList.contains(tagId))//新增tag
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
                if (articleTags.size()>0){
                    articleTagService.saveBatch(articleTags);
                }else if (articleTags.size()==0){
                    List<ArticleTag> articleTags2 = tagList.stream()
                            .filter(tagId->!articleVo.getTags().contains(tagId))//减少tag
                            .map(tagId -> new ArticleTag(article.getId(), tagId))
                            .collect(Collectors.toList());
                    for (ArticleTag articleTag : articleTags2) {
                        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(ArticleTag::getArticleId,articleVo.getId());
                        queryWrapper.eq(ArticleTag::getTagId,articleTag.getTagId());
                        articleTagService.remove(queryWrapper);
                    }
                }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        /**
         * 删除文章
         */
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        remove(queryWrapper);

        /**
         * 接触文章对应的标签关系
         */
        //获取tag集合
        LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = articleTagService.list(queryWrapper1);
        for (ArticleTag articleTag : list) {
            LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ArticleTag::getArticleId,articleTag.getArticleId());
            wrapper.eq(ArticleTag::getTagId,articleTag.getTagId());
            articleTagService.remove(wrapper);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult like(LikeDto likeDto) {
        Integer likenum=redisCache.getCacheMapValue("article:likeNum",likeDto.getArticleId().toString());
        if(Objects.isNull(likenum)){
            LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Article::getId,likeDto.getArticleId());
            likenum=articleMapper.selectOne(lambdaQueryWrapper).getLikeNum().intValue();
            redisCache.setCacheMapValue("article:likeNum",likeDto.getArticleId().toString(),likenum++);
            return ResponseResult.okResult();

        }
        redisCache.incrementCacheMapValue("article:likeNum",likeDto.getArticleId().toString(),1);
        return ResponseResult.okResult();

    }
}